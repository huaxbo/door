package com.am.cs12.commu.local.client.connectPool;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import org.apache.logging.log4j.Logger;

import com.am.cs12.command.MinaData;;

public class MinaHandler extends IoHandlerAdapter {
	
	private static Logger log = LogManager.getLogger(MinaHandler.class.getName()) ;
	
	private Object synObj ;
	private int connectTimeout ;
	private MinaData minaData ;
	
	public MinaHandler( int connectTimeout){
		this.connectTimeout = connectTimeout ;
		this.synObj = new Object() ;
	}

	/**
	 * 发送命令
	 * @param session
	 * @param minaData
	 */
	public void sendCommand(IoSession session,MinaData minaData){
		session.write(minaData) ;
	}
	
	/**
	 * 取得命令结果
	 * @param waitTimeout
	 */
	public MinaData getAnswer(int waitTimeout){
		synchronized (synObj) {
			try {
				synObj.wait(waitTimeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.minaData;
	}
	
	
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		new MinaConnect().disconnect(session, this.connectTimeout) ;
		log.error(cause.getMessage()) ;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		this.minaData = (MinaData) message;
		synchronized (synObj) {
			if (synObj != null) {
				synObj.notifyAll();
			}
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		log.info("命令已经发送。") ;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		log.info("网络会话已经关闭。") ;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		log.info("网络会话已经创建。") ;
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		log.info("网络会话已经打开。") ;
	}


}
