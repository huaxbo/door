package com.am.cs12.commu.local.client.connectPool;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.commu.local.codec.*;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class MinaConnect {

	/**
	 * 判断会话是否有效
	 * @param se
	 * @return
	 */
	protected boolean isConnected(IoSession se) {
		return (se != null && se.isConnected());
	}
	
	/**
	 * 创建新会话
	 * @param url
	 * @param port
	 * @return
	 */
	protected IoSession createSession(String host , int port , int connectTimeout , MinaHandler handler) throws Exception{
		SocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new LocalCodecFactory()));
		connector.setHandler(handler);
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
		connectFuture.awaitUninterruptibly(connectTimeout);
		IoSession se = connectFuture.getSession();
		return se ;
	}
	

	/**
	 * 关闭会话联接
	 * @param se
	 * @param connectTimeout
	 */
	protected void disconnect(IoSession se , int connectTimeout) {
		if (se != null) {
			try{
				se.close(true).awaitUninterruptibly(connectTimeout);
			}catch(Exception e){
			}finally{
				se = null;
			}
		}
	}
}
