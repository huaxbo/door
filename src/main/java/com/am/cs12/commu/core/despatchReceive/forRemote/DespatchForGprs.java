package com.am.cs12.commu.core.despatchReceive.forRemote;

import java.net.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.mina.core.session.IoSession;

import com.am.common.threadPool.ACThreadJob;
import com.am.cs12.config.*;
import com.am.cs12.config.meter.MeterAction;
import com.am.cs12.command.Command;
import com.am.cs12.commu.protocol.* ;
import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.DealCachCommandForGprsSerail;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.CommandTypeForGprsSerial;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.commu.core.log.AmLog;
import com.am.cs12.commu.core.CoreServer;
import com.am.cs12.util.AmConstant;
import com.automic.door.util.cmder.RltCache;
import com.automic.door.web.app.DoorVO;
import com.automic.global.util.ConstantGlo;

public class DespatchForGprs {
	
	private static Logger log = LogManager.getLogger(DespatchForGprs.class.getName()) ;
	/**
	 * 接收到远程数据
	 * @param session
	 * @param data
	 */
	public void receiveData(IoSession session, byte[] data){
		if(data == null){
			log.error("出错 ，收到测控器的数据为空！") ;
			return ;
		}
		String dataHex = null ;
		try {
			dataHex = new UtilProtocol().byte2Hex(data , true).toUpperCase() ;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("将数据转换为十六进制时出错！" ) ;
		}finally{}
		//数据处理
		String id = null;
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		if(!rsm.hasSession(session)){
			//会话管理器中不存在此会话，说明刚建立网络连接，根据通信规约，此数据应该是上线数据
			log.info("\n<<<<<<<<收到测控器数据:" + dataHex) ;
			if(this.dealOnLine(session, data, rsm)){
				id = rsm.getRtuId(session) ;
				this.saveMeterStatus(session, id, data) ;
				this.dealData(id, data, dataHex) ;	
				AmLog.log(id, "上线数据:" + dataHex) ;
			}
		}else{
			//会话管理器中存在此会话，
			id = rsm.getRtuId(session) ;
			this.saveMeterStatus(session, id, data) ;
			this.dealData(id, data, dataHex) ;	
			AmLog.log(id, "上报数据:" + dataHex) ;
		}
	}
	
	/**
	 * 更新测控终端状态
	 * @param id
	 * @param data
	 */
	protected void saveMeterStatus(IoSession session, String id , byte[] data){
		InetSocketAddress sa = (InetSocketAddress)session.getRemoteAddress() ;
		int meterPort = sa.getPort() ;
		String meterIp = sa.getAddress().getHostAddress();
		MeterStatusManager.instance().receivedMeterData(id, meterIp, meterPort, data.length) ;
	}
	
	/**
	 * 处理上线
	 * @param session
	 * @param data
	 */
	protected boolean dealOnLine(IoSession session, byte[] data , RemoteSessionManager rsm){
		boolean isException = false ;
		if(!rsm.hasSession(session)){
			//会话管理器中不存在此会话，说明刚建立网络连接，根据通信规约，此数据应该是上线数据
			HandleOnLine online = HandleOnLine.instance() ;
			try {
				String id = online.match(data) ;
				if(id == null){
					log.error("严重错误，从上线数据中未得到 RTU ID ！" ) ;
					isException = true ;
				}else{
					rsm.putSession(id, session) ;
					log.info("RTU(ID=" + id + ")上线了。") ; ;
				}
			} catch (Exception e) {
				log.error("严重错误，分析上线数据时产生异常 ！" ) ;
				e.printStackTrace();
				isException = true ;
			}finally{
				if(isException){
					rsm.closeSession(session) ;
				}
			}
		}
		return !isException ;
	}
	
	/**
	 * 处理数据
	 * @param rsm
	 * @param data
	 * @param dataHex
	 */
	protected void dealData(String id , byte[] data , String dataHex){
		this.threadPoolDealData(id, data, dataHex) ;
	}


	/**
	 * 线程池处理数据
	 * @param id
	 * @param data
	 * @param dataHex
	 */
	protected void threadPoolDealData(final String id, final byte[] data, final String dataHex){
		try {
			CoreServer.remotePool.SetThreadJob(new ACThreadJob() {
				public void stop(){}
				public void execute() {
					if(id == null){
						log.error("严重错误，未能从网络会话中得到RTU ID，从而不能处理其上报数据。") ;
						return ;
					}
					this.dealData(id, data, dataHex) ;
				}
				/**
				 * 处理数据
				 * @param id
				 * @param session
				 * @param data
				 * @param rsm
				 */
				private void dealData(String id, byte[] data , String dataHex){
					log.info("\n<<<<<<<<收到测控终端(ID:" + id + ")数据:" + dataHex) ;
					DriverMeter rtuDriver = this.getRtuDriver(id) ;
					if(rtuDriver == null){
						log.error("严重错误，未能得到 ID为" + id + "的测控终端协议驱动。") ;
					}
					Action action = rtuDriver.analyseData(id, data, dataHex) ;
					if (action.has(Action.remoteConfirm)) {
						//需要向远端测控器回复确认
						byte[] buf = rtuDriver.getRemoteData();
						if(buf != null){
							try {
								DealCachCommandForGprsSerail.instance().setConfirm(
										id, 
										rtuDriver.getCommandCode(), 
										buf, 
										AmConstant.channel_gprs) ;
							} catch (Exception e) {
								log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
							}finally{}
						}
					}
					if (action.has(Action.remoteCommand)) {
						//需要向远端测控器发送命令
						byte[] buf = rtuDriver.getRemoteData();
						if(buf != null){
							try {
								DealCachCommandForGprsSerail.instance().setCommand(
										CommandTypeForGprsSerial.CommandType.byteCom ,
										CommandTypeForGprsSerial.CommandSendPriority.no ,
										CommandTypeForGprsSerial.CommandSendTimes.repeat ,
										CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_wait,
										null,
										id, 
										rtuDriver.getCommandCode(), 
										buf) ;
							} catch (Exception e) {
								log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
							}finally{}
						}
					}
					if (action.has(Action.commandResult)) {
						//命令结果，需要向中心系统发送
						Data d = rtuDriver.getCenterData();
						String code = rtuDriver.getDataCode() ;
//						String commandId = null;
						if (d != null) {
							//向中心系统发送数据
							if(code == null){
								log.error("出错，未能从协议分析器驱动得到上报数据的功能码，从而不能得到数据对应命令及命令ID。" ) ;
							}else{
								/*commandId = DealCachCommandForGprsSerail.instance().successCommand(id, code, AmConstant.channel_gprs);
								d.setCommandId(commandId) ;
								if(d.getCommandId() == null){
									log.warn("警告，未能从命令缓存中得到命令的ID，从而不能在命令结果中设置命令ID！") ;									
								}else{
									//命令结果通知
									CmdRlt.singleInstance().putCmdRlt(commandId, d) ;
								}*/
								//F1查询结果缓存
								DealCachCommandForGprsSerail.instance().successCommand(id, code, AmConstant.channel_gprs);
								if(code.equals(Code206.cd_F1)){
						    		RltCache.updateData(id, d.getSubData());
						    		log.info("设备[" + id + "]F1结果更新缓存成功！");
								}else{
						    		log.warn("设备[" + id + "]" + code + "结果暂无处理！");
								}
							}
							//加入 id
							d.setId(id) ;
							//加入数据的十六进制
							d.setHex(dataHex) ;
														
							/*****/
							//发布数据到数据库接口表
//							new DespatchForPublish().publishResult(id, d, code) ;							
						}
					}
					if (action.has(Action.commandWait)) {
						//命令有多个结果，需要等待后续结果
						String code = rtuDriver.getDataCode() ;
						if(code == null){
							log.error("出错，未能从协议分析器驱动得到上报数据的功能码，从而不能得到数据对应命令及命令ID。" ) ;
						}else{
							DealCachCommandForGprsSerail.instance().lengthenCommandLife(id, code, AmConstant.channel_gprs) ;
						}
					}
					if (action.has(Action.autoReport)) {
						//主动上报数据，需要向中心系统发送
						Data d = rtuDriver.getCenterData();
						String code = rtuDriver.getDataCode() ;
						if (d != null) {
							//向中心系统发送数据
							//加入id
							d.setId(id) ;
							//加入数据的十六进制
							d.setHex(dataHex) ;

							DealCachCommandForGprsSerail.instance().notifyCanSendCommand(id, AmConstant.channel_gprs) ;
							//保存测控终端状态
							MeterStatusManager.instance().atuoReportData(id, rtuDriver.getMeterWorkModel()) ;
						}
					}
					if (action.has(Action.changeId)) {
						//这个判断及处理一个放在Action.commandResult处理代码之后
						//需要改变ID
						String newId = rtuDriver.getNewId() ;
						new MeterAction().updateMeterId(id, newId) ;
					}
					if (action.has(Action.synchronizeClock)) {
						//同步测控终端与通信服务器时钟
						byte[] buf = rtuDriver.createSetClockCommandDirect(rtuDriver.getId(), this.getKeyPassword(id)) ;
						if(buf != null){
							try {
								DealCachCommandForGprsSerail.instance().setCommand(
										CommandTypeForGprsSerial.CommandType.byteCom ,
										CommandTypeForGprsSerial.CommandSendPriority.no ,
										CommandTypeForGprsSerial.CommandSendTimes.single ,
										CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_delete,
										Command.defaultId,
										id, 
										Code206.cd_11, 
										buf) ;
							} catch (Exception e) {
								log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
							}finally{}
						}
					}
					
					if (action.has(Action.closeConnect)) {
						//RTU 注消请求
						log.error("出错，分析RTU数据后，不会出现closeConnect动作类型！");
					}
					if (action.has(Action.unknown)) {
						//无法识别的功能码
						DealCachCommandForGprsSerail.instance().notifyCanSendCommand(id, AmConstant.channel_gprs) ;
						log.error("出错，分析RTU数据时，出现无法识别的功能码！");
					}
					if (action.has(Action.noAction)) {
						//没动作
						DealCachCommandForGprsSerail.instance().notifyCanSendCommand(id, AmConstant.channel_gprs) ;
						log.info("分析RTU数据后，无动作。");
					}
					if (action.has(Action.error)) {
						//出错了
						DealCachCommandForGprsSerail.instance().notifyCanSendCommand(id, AmConstant.channel_gprs) ;
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
				
				/**
				 * 得到RTU 密钥与密码
				 * @param id
				 * @return
				 */
				private Integer[] getKeyPassword(String id){
					return new MeterHelp().getKeyPassword(id) ;
				}
				
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
