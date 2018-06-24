package com.am.cs12.commu.core.despatchReceive.forRemote;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.common.threadPool.ACThreadJob;
import com.am.cs12.commu.core.CoreServer;
import com.am.cs12.commu.core.despatchReceive.forPublish.DespatchForPublish;
import com.am.cs12.commu.core.log.AmLog;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.commu.protocol.Action;
import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.config.MeterHelp;
import com.am.cs12.config.meter.MeterAction;
import com.am.cs12.commu.core.remoteCommand.forSM.DealCachCommandForSM;

import com.am.util.NumberUtil;

public class DespatchForSM {
	private static Logger log = LogManager.getLogger(DespatchForSM.class.getName()) ;
	/**
	 * 接收到远程数据
	 * @param session
	 * @param data
	 */
	public void receiveData(String phone , String dataHex){
		if(phone == null || phone.equals("")){
			log.error("出错，收到短信上报，但未能从短信中得到手机号！") ;
			return ;
		}
		if(phone.length() > 11){
			phone = phone.substring(phone.length() - 11 ) ;
		}
		if(dataHex == null){
			log.error("出错，收到测控器(手机号:" + (phone==null?"":phone) + ")的短信上报数据为空！") ;
			return ;
		}
		if(!NumberUtil.isHex(dataHex)){
			log.error("出错，收到测控器(手机号:" + (phone==null?"":phone) + ")的短信上报数据是非法十六进制数据，数据为:" + dataHex) ;
			return ;
		}
		
		log.info("\n收到测控器(手机号:" + (phone==null?"":phone) + ")短信上报数据:" + dataHex) ;

		String rtuId = new MeterHelp().getIdByPhone(phone) ;
		if(rtuId == null){
			log.error("出错，解析测控器(手机号:" + (phone==null?"":phone) + ")的短信上报数据前，不能得到配置的测控终端RTU ID！") ;
			return ;
		}
		AmLog.log(rtuId, "短信上报数据:" + dataHex) ;
		
		this.saveMeterStatus(rtuId) ;
		
		byte[] data = new UtilProtocol().hex2Bytes(dataHex) ;
		if(data == null){
			log.error("出错，将测控器(手机号:" + (phone==null?"":phone) + ")的短信上报数据转换成字节数组时出错！") ;
			return ;
		}
		
		this.dealData(rtuId, data, dataHex) ;
	}
	
	/**
	 * 更新测控终端状态
	 * @param rtuId
	 * @param data
	 */
	private void saveMeterStatus(String rtuId){
		MeterStatusManager.instance().receivedMeterSM(rtuId) ;
	}
	/**
	 * 处理数据
	 * @param rsm
	 * @param data
	 * @param dataHex
	 */
	private void dealData(String rtuId , byte[] data , String dataHex){
		this.threadPoolDealData(rtuId, data, dataHex) ;
	}


	/**
	 * 线程池处理数据
	 * @param rtuId
	 * @param data
	 * @param dataHex
	 */
	private void threadPoolDealData(final String rtuId, final byte[] data, final String dataHex){
		try {
			CoreServer.remotePool.SetThreadJob(new ACThreadJob() {
				public void stop(){}
				public void execute() {
					if(rtuId == null){
						log.error("严重错误，无RTU ID，从而不能处理其上报数据。") ;
						return ;
					}
					this.dealRtuData(rtuId, data, dataHex) ;
				}
				/**
				 * 处理 数据
				 * @param rtuId
				 * @param session
				 * @param data
				 * @param rsm
				 */
				private void dealRtuData(String id, byte[] data, String dataHex){
					log.info("\n<<<<<<<<收到测控终端(RTU-ID:" + id + ")数据:" + dataHex) ;
					DriverMeter rtuDriver = this.getRtuDriver(id) ;
					if(rtuDriver == null){
						log.error("严重错误，未能得到RTU ID为" + id + "的RTU协议驱动。") ;
					}
					Action action = rtuDriver.analyseData(id, data, dataHex) ;
					if (action.has(Action.remoteConfirm)) {
						//需要向远端测控器回复确认
						//byte[] buf = rtuDriver.getRemoteData();
						//当前协议定义，短信数据不需要应答
					}
					if (action.has(Action.remoteCommand)) {
						//需要向远端测控器发送命令
						//byte[] buf = rtuDriver.getRemoteData();
						//当前协议定义，短信数据不需要回复命令
					}
					if (action.has(Action.commandResult)) {
						//命令结果，需要向中心系统发送
						Data d = rtuDriver.getCenterData();
						String code = rtuDriver.getDataCode() ;
						if (d != null) {
							//向中心系统发送数据
							if(code == null){
								log.error("出错，未能从协议分析器驱动得到上报数据的功能码，从而不能得到数据对应命令及命令ID。" ) ;
							}else{
								d.setCommandId(DealCachCommandForSM.instance().successCommand(id, code)) ;
							}
							//加入 id
							d.setId(id) ;
							//加入数据的十六进制
							d.setHex(dataHex) ;
							
							new DespatchForPublish().publishResult(id, d, code) ;
						}
					}
					if (action.has(Action.autoReport)) {
						//主动上报数据，需要向中心系统发送
						Data d = rtuDriver.getCenterData();
						String code = rtuDriver.getDataCode() ;
						if (d != null) {
							//向中心系统发送数据
							//加入 id
							d.setId(id) ;
							//加入数据的十六进制
							d.setHex(dataHex) ;

							new DespatchForPublish().publishResult(id, d, code) ;
							//保存测控终端状态
							MeterStatusManager.instance().atuoReportData(id, rtuDriver.getMeterWorkModel()) ;
						}
					}
					if (action.has(Action.changeId)) {
						//这个判断及处理一个放在Action.commandResult处理代码之后
						//需要改变RTU ID
						String newId = rtuDriver.getNewId() ;
						new MeterAction().updateMeterId(id, newId) ;
						//保存测控终端状态
						MeterStatusManager.instance().updateMeterId(newId, id) ;
					}
					if (action.has(Action.synchronizeClock)) {
						//同步测控终端与通信服务器时钟
						//byte[] buf = rtuDriver.createSetClockCommandDirect(rtuDriver.getRtuId(), this.getRtuKeyPassword(id)) ;
						//当前协议定义，短信数据不需要回复命令
					}
					
					if (action.has(Action.closeConnect)) {
						//RTU 注消请求
						log.error("出错，分析RTU数据后，不会出现closeConnect动作类型！");
					}
					if (action.has(Action.unknown)) {
						//无法识别的功能码
						log.error("出错，分析RTU数据时，出现无法识别的功能码！");
					}
					if (action.has(Action.noAction)) {
						//没动作
						log.info("分析RTU数据后，无动作。");
					}
					if (action.has(Action.error)) {
						//出错了
						log.error("分析RTU数据出错：" + rtuDriver.getError());
					}
				}
				
				/**
				 * 得到RTU 驱动
				 * @param localSession
				 * @param id
				 * @return
				 */
				private DriverMeter getRtuDriver(String id){
					MeterHelp mdh = new MeterHelp() ;
					DriverMeter driver = mdh.getRtuDriver(id) ;
					if(driver == null){
						log.error(mdh.getError()) ;
					}
					return driver ;
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
