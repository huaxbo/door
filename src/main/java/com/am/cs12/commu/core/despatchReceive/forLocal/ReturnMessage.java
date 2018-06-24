package com.am.cs12.commu.core.despatchReceive.forLocal;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.command.Command;

public class ReturnMessage {
	private static Logger log = LogManager.getLogger(ReturnMessage.class.getName()) ;
	
	/**
	 * 处理命令成功
	 * @param message
	 */
	protected void successed(IoSession localSession, String message , String commandId, boolean remoteOnLine){
		log.info(message) ;
		if(localSession != null){
			this.returnLocal(localSession, new Command().createReturnSuccessCommand(message, commandId, remoteOnLine)) ;
		}
	}
	/**
	 * 处理命令成功
	 * @param message
	 */
	protected void successed(IoSession localSession, String message , String commandId){
		log.info(message) ;
		if(localSession != null){
			this.returnLocal(localSession, new Command().createReturnSuccessCommand(message, commandId)) ;
		}
	}
	/**
	 * 处理命令发生错误
	 * @param message
	 */
	protected void errored(IoSession localSession, String message){
		log.error(message) ;
		if(localSession != null){
			this.returnLocal(localSession, new Command().createReturnErrorCommand(message, Command.defaultId)) ;
		}
	}
	
	/**
	 * 向业务系统回馈
	 * @param localSession
	 * @param reCom
	 */
	private void returnLocal(IoSession localSession, Command reCom){
		if(localSession != null){
			localSession.write(reCom) ;
		}
	}


}
