package com.am.cs12.commu.core.despatchReceive.forRemote;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.common.threadPool.ACThreadJob;
import com.am.cs12.command.Command;
import com.am.cs12.commu.core.CoreServer;
import com.am.cs12.commu.core.log.AmLog;
import com.am.cs12.commu.core.despatchReceive.forPublish.DespatchForPublish;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.CommandTypeForGprsSerial;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.DealCachCommandForGprsSerail;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.commu.protocol.Action;
import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.commu.protocol.HandleOnLine;
import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.config.MeterHelp;
import com.am.cs12.config.meter.MeterAction;
import com.am.cs12.util.AmConstant;

public class DespatchForSate {
	
	private static Logger log = LogManager.getLogger(DespatchForSate.class.getName()) ;

	/**
	 *  接收到远程数据
	 * @param satelliteId
	 * @param dataHex
	 */
	public void receiveData(String satelliteId, byte[] data){
		if(satelliteId == null || satelliteId.equals("")){
			log.error("出错，收到卫星上报，但未能从数据中得到测控终端的卫星用户ID！") ;
			return ;
		}
		if(data == null){
			log.error("出错，收到测控器(卫星用户ID:" + (satelliteId==null?"":satelliteId) + ")的卫星上报数据为空！") ;
			return ;
		}
		String dataHex = null ;
		try {
			dataHex = new UtilProtocol().byte2Hex(data , true).toUpperCase() ;
			log.info("\n收到测控器(卫星用户ID:" + (satelliteId==null?"":satelliteId) + ")卫星上报数据:" + dataHex) ;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("将数据转换为十六进制时出错！" ) ;
		}

		String id = new MeterHelp().getIdBySatelliteId(satelliteId) ;
		if(id == null){
			log.error("出错，解析测控器(卫星用户ID:" + (satelliteId==null?"":satelliteId) + ")的卫星上报数据前，不能得到配置的测控终端RTU ID！") ;
			return ;
		}
		AmLog.log(id, "卫星上报数据:" + dataHex) ;
		
		this.saveMeterStatus(id) ;
		
		this.dealData(id, data, dataHex) ;
	}
	
	/**
	 * 更新测控终端状态
	 * @param id
	 * @param data
	 */
	private void saveMeterStatus(String id){
		MeterStatusManager.instance().receivedMeterSatellite(id) ;
	}
	/**
	 * 处理数据
	 * @param rsm
	 * @param data
	 * @param dataHex
	 */
	private void dealData(String id , byte[] data , String dataHex){
		this.threadPoolDealData(id, data, dataHex) ;
	}


	/**
	 * 线程池处理数据
	 * @param id
	 * @param data
	 * @param dataHex
	 */
	private void threadPoolDealData(final String id, final byte[] data, final String dataHex){
		try {
			CoreServer.remotePool.SetThreadJob(new ACThreadJob() {
				public void stop(){}
				public void execute() {
					if(id == null){
						log.error("严重错误，无RTU ID，从而不能处理其上报数据。") ;
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
				private void dealData(String id, byte[] data, String dataHex){
					
					//设备id验证
					HandleOnLine online = HandleOnLine.instance() ;
					try{
						String remoteId = online.match(data) ;
						if(remoteId != null && !remoteId.equals(id)){
							log.error("\n$$$$$$$$:上报数据ID(" + remoteId + ")与卫星配置ID(" + id + ")不一致，不做处理！");
							log.error("\n收到数据：\n" + dataHex);
							return ;
						}
					}catch(Exception e){
						
					}finally{}
					
					log.info("\n<<<<<<<<收到测控终端(ID:" + id + ")数据:" + dataHex) ;
					DriverMeter rtuDriver = this.getRtuDriver(id) ;
					if(rtuDriver == null){
						log.error("严重错误，未能得到测控终端ID为" + id + "的协议驱动。") ;
					}
					Action action = rtuDriver.analyseData(id, data, dataHex) ;
					if (action.has(Action.stateReportConfirm)) {//定时上报命令确认
						//需要向远端测控器回复确认
						//由于卫星终端机发送命令间隔必须>1分钟
						byte[] buf = rtuDriver.getRemoteData();
						if(buf != null){
							try {
								DealCachCommandForGprsSerail.instance().setConfirm(
										id, 
										rtuDriver.getCommandCode(), 
										buf, 
										AmConstant.channel_serialPort) ;
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
						if (d != null) {
							//向中心系统发送数据
							if(code == null){
								log.error("出错，未能从协议分析器驱动得到上报数据的功能码，从而不能得到数据对应命令及命令ID。" ) ;
							}else{
								d.setCommandId(DealCachCommandForGprsSerail.instance().successCommand(id, code, AmConstant.channel_serialPort)) ;
								if(d.getCommandId() == null){
									log.warn("警告，未能从命令缓存中得到命令的ID，从而不能在命令结果不设置命令ID！") ;
								}
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
							DealCachCommandForGprsSerail.instance().notifyCanSendCommand(id, AmConstant.channel_serialPort) ;
							//保存测控终端状态
							MeterStatusManager.instance().atuoReportData(id, rtuDriver.getMeterWorkModel()) ;
						}
					}
					if (action.has(Action.changeId)) {
						//这个判断及处理一个放在Action.commandResult处理代码之后
						//需要改变 ID
						String newId = rtuDriver.getNewId() ;
						new MeterAction().updateMeterId(id, newId) ;
						//保存测控终端状态
						MeterStatusManager.instance().updateMeterId(newId, id) ;
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
			e.printStackTrace();
		}
	}
}
