package com.am.cs12.commu.core.despatchReceive.forLocal;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.command.Command;
import com.am.cs12.commu.protocol.Action;
import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.config.MeterHelp;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class DealCommandForSM {
	private static Logger log = LogManager.getLogger(DealCommandForGprsSerial.class.getName()) ;
	
	/**
	 * 处理RTU命令
	 * @param localSession 与业务系统的网络连接会话
	 * @param com 命令
	 * @param knowRemoteOnLine 是否知道远程测控器应该在线（如果是常在线的方式，必然知道应该是在线，如果存在低功耗测控器，那么就不能知道在线情况）
	 */
	protected void dealCommand(IoSession localSession, Command com, String rtuId, String code){

		DriverMeter rtuDriver = this.getRtuDriver(localSession, rtuId) ;
		
		if(rtuDriver != null ){
			//已经知道应该在线（应该在线：按照协议应该在线，实际上可能在线也可能不在线）
			Action action = rtuDriver.createCommand(com, this.getKeyPassword(rtuId)) ;
			if (action.has(Action.remoteCommand)) {
				//向远端发送数据
				byte[] buf = rtuDriver.getRemoteData() ;
				if(buf != null){
					this.sendByteSm(localSession, com.getId(), rtuId, code, buf) ;
				}else{
					String content = rtuDriver.getRemoteDataStr() ;
					this.sendStringSm(localSession, com.getId(), rtuId, code, content) ;
				}
			}else if(action.has(Action.unknown)){
				new ReturnMessage().errored(localSession , "出错，不能识别的协议命令功能码" + code + "！") ;
				return ;
			}else if (action.has(Action.error)) {
				//发生错误，向中心业务系统返回错误信息
				new ReturnMessage().errored(localSession , "出错，" + rtuDriver.getError()) ;
				return ;
			}else{
				new ReturnMessage().errored(localSession , "出错，协议驱动构建命令时返回动作类型不是所期望！") ;
				return ;
			}
		}
	}
	
	/**
	 * 发送短信a
	 */
	private void sendByteSm(IoSession localSession, String commandId, String rtuId, String code, byte[] buf){
		Exception exc = null ;
		String content = null ;
		try {
			content = new UtilProtocol().byte2Hex(buf, false) ;
		} catch (Exception e) {
			exc = e ;
			log.error("将字节命令转换成十六进制出错：" + e.getMessage()) ;
		}finally{}
	}
	/**
	 * 发送短信 
	 */
	private void sendStringSm(IoSession localSession, String commandId, String rtuId, String code, String content){}
	
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

	/**
	 * 得到测控终端手机号 
	 * @param localSession
	 * @param id
	 * @return
	 */
	private String getMeterPhone(String id){
		MeterHelp mdh = new MeterHelp() ;
		return mdh.getMeterPhone(id) ;
	}
	
}