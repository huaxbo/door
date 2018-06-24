package com.am.cs12.commu.protocol.amRtu206;

import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import com.am.cs12.command.Command;
import com.am.cs12.commu.protocol.Action;
import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.commu.protocol.ProtocolParam;
import com.am.cs12.commu.protocol.amRtu206.cd00.Data206_cd00;
import com.am.cs12.commu.protocol.amRtu206.cd02.Data206_cd02;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.HeadProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.TailProtocol;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.amRtu206.util.FileHelp;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.config.MeterHelp;

public class Driver206 extends DriverMeter {

	private static Logger log = LogManager.getLogger(Driver206.class);
	protected Data centerData;

	/**
	 * 带参数的构造方法，参数为协议名称 同时启动发送心跳的线程
	 * 
	 * @param protocolName
	 *            协议名称
	 */
	public Driver206(String protocolName) {
		super(protocolName);
	}

	/**
	 * 分析RTU数据
	 * 
	 * @param rtuId
	 * @param b
	 * @return
	 */
	@Override
	public Action analyseData(String rtuId, byte[] b, String dataHex) {
		try {
			DataProtocol206 dp = new DataProtocol206();

			// 进行控制域分析
			ControlProtocol cp = new ControlProtocol().parseControl(b);

			Action action = Action.nullAction;
			// 得到数据中的功能码
			this.dataCode = dp.getDataCode(b, cp);

			// 检查数据头合法性
			int dataLen = 0;
			if (this.dataCode.equalsIgnoreCase(Code206.cd_61)) {
				dataLen = new HeadProtocol().checkImageDataLen(b);
			} else {
				dataLen = new HeadProtocol().checkHeadAndGetDataLen(b);
			}
			// 检查数据尾合法性
			new TailProtocol().checkTail(b, dataLen);

			if (this.dataCode == null) {
				log.error("出错，未能得到RTU数据中的功能码，不能分析数据。");
				action = action.add(action, Action.noAction);
			}
			if (this.dataCode.equalsIgnoreCase(Code206.cd_00)) {
				// 通信服务器内部回还数据，模拟RTU上报
				this.centerData = dp.parseRecircle_00(rtuId, b, cp,
						this.dataCode);

				// 偷梁换柱，更换功能码为 RTU所确认的功能码，这样一来使得上位机下发命令功能码与回还功能码一致了
				this.dataCode = this.centerData.getCode();
				if (this.centerData.getCode().equalsIgnoreCase(Code206.cd_61)) {
					// 这是回还图像数据
					Object o = this.centerData.getSubData();
					if (o != null) {
						Data206_cd00 subD = (Data206_cd00) o;
						String picFilePath = subD.getContent();
						byte[] addtion = new FileHelp().getFile(picFilePath);
						subD.setAddtion(addtion);
						if (addtion == null) {
							log.info("图片数据为空！");
							action = action.add(action, Action.noAction);
						} else if (addtion[0] != (byte) 0xFF
								|| addtion[1] != (byte) 0xD8) {// 图片头校验失败
							UtilProtocol up = new UtilProtocol();
							log.info("图片数据头校验失败！头数据为："
									+ up.byte2Hex(
											new byte[] { addtion[0], addtion[1] },
											true).toUpperCase());
							action = action.add(action, Action.noAction);
						} else {
							String sdt = picFilePath.substring(
									picFilePath.lastIndexOf("-") + 1,
									picFilePath.lastIndexOf('.'));
							this.centerData.setClock(sdt.substring(0, 4) + "-"
									+ sdt.substring(4, 6) + "-"
									+ sdt.substring(6, 8) + " "
									+ sdt.substring(8, 10) + ":"
									+ sdt.substring(10, 12) + ":"
									+ sdt.substring(12, 14));
							action = action.add(action, Action.commandResult);
						}
					}
				} else {
					action = action.add(action, Action.noAction);
				}

			} else

			if (this.dataCode.equalsIgnoreCase(Code206.cd_01)) {
				// RTU 上报的确认
				this.centerData = dp.parseConfirm_01(rtuId, b, cp,
						this.dataCode);
				action = action.add(action, Action.commandResult);
				// 偷梁换柱，更换功能码为 RTU所确认的功能码，这样一来使得上位机下发命令功能码与RTU上传数据的功能码一致了
				this.dataCode = this.centerData.getCode();
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_02)) {
				// 有关RTU 链路检测
				this.centerData = dp
						.parseReport_02(rtuId, b, cp, this.dataCode);
				if (this.centerData != null) {
					// 得到从上报数据中分析出来的RTU ID
					Data206_cd02 subD = (Data206_cd02) this.centerData
							.getSubData();
					Integer flag = subD.getFlag();
					if (flag.intValue() == (byte) Constant.Flag_f0_forCode2) {
						// 上线（登录）
						this.id = this.centerData.getId();
						this.remoteData = new CommandProtocol206().confirm(
								this.id, "F2", null, cp.DIVS,
								this.getKeyPassword(this.id));
						this.commandCode = Code206.cd_02;
						action = action.add(action, Action.remoteConfirm);
					} else if (flag.intValue() == (byte) Constant.Flag_f1_forCode2) {
						// 下线(退出登录)
					} else if (flag.intValue() == (byte) Constant.Flag_f2_forCode2) {
						// 在线保持
					}
				}
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_50)) {
				// 应答查询RTU ID
				this.centerData = dp.parseRead_50(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_51)) {
				// 应答查询遥测终端、中继站时钟
				this.centerData = dp.parseRead_51(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_52)) {
				// 应答查询遥测终端、中继站工作模式
				this.centerData = dp.parseRead_52(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_11)) {
				// 设置遥测终端、中继站时钟
				this.centerData = dp.parseRead_51(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_12)) {
				// 设置遥测终端、中继站模式
				this.centerData = dp.parseRead_52(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_10)) {
				// 设置遥测终端、中继站地址
				this.centerData = dp.parseRead_50(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_F1)) {
				// 门操作
				this.centerData = dp.parseRead_F1(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			} else if (this.dataCode.equalsIgnoreCase(Code206.cd_F4)) {
				// 门状态
				this.centerData = dp.parseRead_F4(rtuId, b, cp, this.dataCode);
				action = action.add(action, Action.commandResult);
			}

			//

			// 是否进行时钟同步
			if (dp.isNeedSynchronizeClock()) {
				action = action.add(action, Action.synchronizeClock);
			}

			if (this.centerData != null) {
				// 得到从上报数据中分析出来的RTU ID
				this.id = this.centerData.getId();
			}
			return action == null ? Action.noAction : action;

		} catch (Exception e) {
			log.error(e.getMessage());
			this.error = e.getMessage();
			return Action.error;
		}
	}

	/**
	 * 构造针对RTU(测控终端)的命令
	 * 
	 * @param com
	 *            命令
	 * @param key_password
	 *            RTU的密钥与密码
	 * @return
	 */
	@Override
	public Action createCommand(Command com, Integer[] key_password) {
		try {
			HashMap<String, Object> params = com.getParams();
			if (params == null || params.size() == 0) {
				this.error = "出错，命令中参数集合为空，不能构建任何命令！";
				log.error(error);
				return Action.error;
			}
			ProtocolParam pp = (ProtocolParam) params.get(ProtocolParam.KEY);
			this.id = pp.getId();
			if (this.id == null || this.id.trim().equals("")) {
				this.error = "出错，命令中RTU ID为空，不能构建任何命令！";
				log.error(error);
				return Action.error;
			}
			this.commandCode = pp.getCode();
			if (this.commandCode == null || this.commandCode.trim().equals("")) {
				this.error = "出错，命令中功能码为空，不能构建任何命令！";
				log.error(error);
				return Action.error;
			}

			Action action = Action.nullAction;
			CommandProtocol206 cp = new CommandProtocol206();

			if (this.commandCode.equalsIgnoreCase(Code206.cd_11)) {
				// 设置遥测终端、中继站时钟
				this.remoteData = cp.write_11(this.id, params, key_password);
				action = Action.remoteCommand;
			} else if (this.commandCode.equalsIgnoreCase(Code206.cd_12)) {
				// 设置遥测终端、中继站工作模式
				this.remoteData = cp.write_12(this.id, params, key_password);
				action = Action.remoteCommand;
			} else if (this.commandCode.equalsIgnoreCase(Code206.cd_50)) {
				// 查询遥测终端、中继站地址
				this.remoteData = cp.read_50(this.id, params, key_password);
				action = Action.remoteCommand;
			} else if (this.commandCode.equalsIgnoreCase(Code206.cd_51)) {
				// 查询遥测终端、中继站时钟
				this.remoteData = cp.read_51(this.id, params, key_password);
				action = Action.remoteCommand;
			} else if (this.commandCode.equalsIgnoreCase(Code206.cd_52)) {
				// 查询遥测终端、中继站工作模式
				this.remoteData = cp.read_52(this.id, params, key_password);
				action = Action.remoteCommand;
			} else if (this.commandCode.equalsIgnoreCase(Code206.cd_F1)) {
				// 门控制
				this.remoteData = cp.write_F1(this.id, params, key_password);
				action = Action.remoteCommand;
			} else {
				this.error = "出错，命令中功能码(" + this.commandCode + ")不能被识别！";
				log.error(error);
				action = Action.unknown;
			}
			return action == null ? Action.noAction : action;
		} catch (Exception e) {
			log.error(e.getMessage());
			this.error = e.getMessage();
			return Action.error;
		}
	}

	/**
	 * 得到RTU 密钥与密码
	 * 
	 * @param id
	 * @return
	 */
	private Integer[] getKeyPassword(String id) {
		return new MeterHelp().getKeyPassword(id);
	}

	/**
	 * 直接创建复位命令
	 * 
	 * @param id
	 * @param key_password
	 *            RTU的密钥与密码
	 * @return
	 */
	@Override
	public byte[] createSetClockCommandDirect(String id, Integer[] keyPassword) {
		return null;
	}

	/**
	 * 得到命令的功能码，
	 * 
	 * @return
	 */
	@Override
	public String getCommandCode() {
		return this.commandCode;
	}

	/**
	 * 得到数据中的功能码，
	 * 
	 * @return
	 */
	@Override
	public String getDataCode() {
		return this.dataCode;
	}

	/**
	 * 得到发向远程测控器的命令数据
	 * 
	 * @return
	 */
	@Override
	public byte[] getRemoteData() {
		return remoteData;
	}

	@Override
	public String getRemoteDataStr() {
		return remoteDataStr;
	}

	/**
	 * 得到下发命令时设置的ID，或上报数据中的ID
	 * 
	 * @return
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * 得到处理完毕要发向中心业务系统的数据
	 * 
	 * @return
	 */
	@Override
	public Data getCenterData() {
		return this.centerData;
	}

	/**
	 * 错误消息
	 * 
	 * @return
	 */
	@Override
	public String getError() {
		return error;
	}

	/**
	 * 得到测控器工作模式
	 */
	@Override
	public Integer getMeterWorkModel() {
		return this.meterWorkModel;
	}

}
