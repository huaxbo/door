package com.am.cs12.commu.core.despatchReceive.forLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.command.Command;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.*;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.commu.protocol.Action;
import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.config.MeterHelp;
import com.am.cs12.config.MeterVO;
import com.am.cs12.util.AmConstant;

public class DealCommandForGprsSerial {
	private static Logger log = LogManager.getLogger(DealCommandForGprsSerial.class.getName()) ;
	
	
	/**
	 * 处理命令
	 * @param localSession 与中心业务系统的网络连接会话
	 * @param meterOnlineAlways 根据meter.xml配置文件配置，测控终端是否常在线
	 * @param com 命令
	 * @param id
	 * @param code
	 */
	protected void dealCommand(IoSession localSession, boolean meterOnlineAlways, Command com, String id, String code){

		DriverMeter rtuDriver = this.getRtuDriver(localSession, id) ;
		RemoteSessionManager rsm1 = RemoteSessionManager.instance() ;
		final IoSession session1 = rsm1.getSession(id) ;
		
		if(rtuDriver != null ){
			Exception ex = null ;
			if(!meterOnlineAlways && session1 == null){
				//根据配置，远程测控器不常在线(低功耗并且无卫星通道)，缓存原生命令
				try {
					DealCachCommandForGprsSerail.instance().setCommand(
							CommandTypeForGprsSerial.CommandType.objCom ,
							CommandTypeForGprsSerial.CommandSendPriority.no ,
							CommandTypeForGprsSerial.CommandSendTimes.single ,
							CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_wait,
							com.getId(),
							id, 
							code, 
							com) ;
				} catch (Exception e) {
					ex = e ;
					log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
				}finally{
					if(ex == null){
						new ReturnMessage().successed(localSession, "成功缓存了命令，并等待命令发送。", com.getId(), RemoteSessionManager.instance().isConnect(id)) ;
					}else{
						new ReturnMessage().errored(localSession , "出错，缓存命令发生异常:" + ex.getMessage()) ;
					}
				}
			}else{
				//根据配置，远程测控器常在线 
				
				MeterHelp mh = new MeterHelp() ;
				MeterVO meter = mh.getMeter(id) ;
				
				RemoteSessionManager rsm = RemoteSessionManager.instance() ;
				final IoSession session = rsm.getSession(id) ;
				if(!meter.mainChannel.equals(AmConstant.channel_serialPort)
						&& !mh.containChannel(id , AmConstant.channel_serialPort)
						&& session == null){
					//主信道及辅信道都无卫星，并且GPRS 无会话
					//缓存原生命令
					try {
						DealCachCommandForGprsSerail.instance().setCommand(
								CommandTypeForGprsSerial.CommandType.objCom ,
								CommandTypeForGprsSerial.CommandSendPriority.no ,
								CommandTypeForGprsSerial.CommandSendTimes.single ,
								CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_wait,
								com.getId(),
								id, 
								code, 
								com) ;
					} catch (Exception e) {
						ex = e ;
						log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
					}finally{
						if(ex == null){
							new ReturnMessage().successed(localSession, "成功缓存了命令，并等待命令发送。", com.getId(), RemoteSessionManager.instance().isConnect(id)) ;
						}else{
							new ReturnMessage().errored(localSession , "出错，缓存命令发生异常:" + ex.getMessage()) ;
						}
					}
				}else{
					//通过GPRS或卫星能够即时发命令(字节命令)了
					Action action = rtuDriver.createCommand(com, 
							//new Integer[]{Constant.rtuPassword.get(id),Constant.rtuPassword.get(id)});
							this.getKeyPassword(id)) ;
					if (action.has(Action.remoteCommand)) {
						//动作类型是向远端发送数据
						byte[] buf = rtuDriver.getRemoteData() ;
						if(buf != null){
							String comCode = rtuDriver.getCommandCode() ;
							CommandTypeForGprsSerial.CommandSendTimes stimes = CommandTypeForGprsSerial.CommandSendTimes.repeat ;
							if(comCode.equals(Code206.cd_11)) {
								//设置时钟，不重复发，因为重复发时，时钟已经不准了
								//读取图像(照片)数据，不重复发，因为一次读，要多次上报数据，把多次上报数据组合起来，最后才形成命令结果，这样一来得到命令结果用时较长，系统一定会认为命令结果未上来，所以就会重发命令，重发的话，就再次读图像数据了，必然会混乱的。
								stimes =  CommandTypeForGprsSerial.CommandSendTimes.single ;
							}
							try {
								DealCachCommandForGprsSerail.instance().setCommand(
										CommandTypeForGprsSerial.CommandType.byteCom ,
										CommandTypeForGprsSerial.CommandSendPriority.no ,
										stimes ,
										CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_delete,
										com.getId(),
										id, 
										comCode, 
										buf) ;
							} catch (Exception e) {
								ex = e ;
								log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
							}finally{
								if(ex == null){
									new ReturnMessage().successed(localSession, "成功构造了命令。", com.getId(), RemoteSessionManager.instance().isConnect(id)) ;
								}else{
									new ReturnMessage().errored(localSession , "出错，构造RTU命令二进制数据发生异常:" + ex.getMessage()) ;
								}
							}
						}
					}else if(action.has(Action.unknown)){
						new ReturnMessage().errored(localSession , "出错，不能识别的RTU协议命令功能码" + code + "！") ;
						return ;
					}else if (action.has(Action.error)) {
						//发生错误，向中心业务系统返回错误信息
						new ReturnMessage().errored(localSession , "出错，" + rtuDriver.getError()) ;
						return ;
					}else{
						new ReturnMessage().errored(localSession , "出错，RTU协议驱动构建命令时返回动作类型不是所期望！") ;
						return ;
					}
				}
			}
		}
	}
	
	/**
	 * 得到RTU 密钥与密码
	 * @param id
	 * @return
	 */
	private Integer[] getKeyPassword(String id){
		return new MeterHelp().getKeyPassword(id) ;
	}
	
		
	/**
	 * 得到RTU 驱动
	 * @param localSession
	 * @param id
	 * @return
	 */
	private DriverMeter getRtuDriver(IoSession localSession, String id){
		MeterHelp mdh = new MeterHelp() ;
		DriverMeter driver = mdh.getRtuDriver(id) ;
		if(driver == null){
			new ReturnMessage().errored(localSession , mdh.getError()) ;
		}
		return driver ;
	}
	
	
}
