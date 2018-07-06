package com.automic.door.util.cmder;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.am.cs12.command.Command;
import com.am.cs12.command.CommandType;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.CommandTypeForGprsSerial;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.DealCachCommandForGprsSerail;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.commu.protocol.Action;
import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.commu.protocol.ProtocolParam;
import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.config.MeterHelp;

public class CmdSender {

	private static Logger log = LogManager.getLogger(CmdSender.class);
	
	
	/**
	 * 检测是否在线
	 * @param dtuId
	 * @return
	 */
	public static boolean isOnline(String dtuId){
		
		return RemoteSessionManager.instance().isConnect(dtuId);
	}
	
	/**
	 * 发送命令
	 * @param dtuId
	 * @param cmdId
	 * @param code
	 * @param params
	 * @return
	 */
	public static boolean sendCmd(String dtuId,String cmdId,String code,HashMap<String,Object> params){
		DriverMeter rtuDriver = getRtuDriver(dtuId) ;
		if(rtuDriver == null){
			
			return false;
		}
		Command com = buildCommand(dtuId,cmdId,params,code);
		//通过GPRS或卫星能够即时发命令(字节命令)了
		Action action = rtuDriver.createCommand(com,new Integer[]{1,2}) ;
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
							dtuId, 
							comCode, 
							buf) ;
				} catch (Exception e) {
					log.error("将命令加入命令缓存出错：" + e.getMessage()) ;
					
					return false;
				}finally{}
			}
		}else if(action.has(Action.unknown)){
			log.error("出错，不能识别的RTU协议命令功能码" + code + "！") ;
			return false;
		}else if (action.has(Action.error)) {
			//发生错误，向中心业务系统返回错误信息
			log.error("出错，" + rtuDriver.getError()) ;
			return false;
		}else{
			log.error( "出错，RTU协议驱动构建命令时返回动作类型不是所期望！") ;
			return false;
		}
		
		return true;
	}
	
	/**
	 * 构建命令对象
	 * @param dtuId
	 * @param cmdId
	 * @param params
	 * @param code
	 * @return
	 */
	private static Command buildCommand(String dtuId,String cmdId,HashMap<String,Object> params,String code){
		Command comd = new Command() ;
		comd.setId(cmdId) ;
		comd.setType(CommandType.outerCommand) ;
		
		comd.setParams(params) ;
	
		ProtocolParam pp = new ProtocolParam() ;
		pp.setId(dtuId) ;
		pp.setCode(code) ;
		params.put(ProtocolParam.KEY , pp) ;
		
		return comd;
	}
	
	/**
	 * 得到RTU 驱动
	 * @param localSession
	 * @param id
	 * @return
	 */
	private static DriverMeter getRtuDriver(String id){
		MeterHelp mdh = new MeterHelp() ;
		DriverMeter driver = mdh.getRtuDriver(id) ;
		if(driver == null){
			log.error("协议驱动为空！");
			
			return null;
		}
		return driver ;
	}
}
