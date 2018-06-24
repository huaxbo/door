package com.am.cs12.commu.local.client.connectPool;

import java.util.*;
import org.apache.mina.core.session.IoSession;


public class MinaConnectPool {
	/**
	 * 空闲会话的池(Hashtable是线程安全的)
	 */
	private static Hashtable<String , Vector<IoSession>> freeSessions = new Hashtable<String ,Vector<IoSession>>();

	/**
	 * 从会话池中得到空闲的会话，如果没有空闲的会话，本着快速反应的原则，新建一个会话，不再等待繁忙会话的释放
	 * @param url
	 * @param port
	 * @return
	 * @throws Exception 
	 */
	public static IoSession getSession(String host , int port , int connectTimeout) throws Exception{
		Vector<IoSession> vecter = freeSessions.get(getKey(host , port)) ;
		if(vecter == null){
			vecter = new Vector<IoSession>() ;
			freeSessions.put(getKey(host , port), vecter) ;
		}
		
		MinaConnect con = new MinaConnect() ;
		
		IoSession se = popSession(vecter , con) ;
		if(se == null){
			MinaHandler handler = new MinaHandler(connectTimeout) ;
			se = createNewSession(host, port, connectTimeout, con, handler) ;
		}
		return se ;
	}
	
	/**
	 * 将会话放回池中
	 * @param host
	 * @param port
	 * @param se
	 */
	public static void freeSession(String host , int port , IoSession se){
		if(se == null){
			return ;
		}
		Vector<IoSession> vecter = freeSessions.get(getKey(host , port)) ;
		if(vecter == null){
			vecter = new Vector<IoSession>() ;
			freeSessions.put(getKey(host , port), vecter) ;
		}
		putSession(vecter, se) ;
	}
	
	/**
	 * 从会话池中取得空闲的会话
	 * @param vecter
	 * @return
	 */
	private static IoSession popSession(Vector<IoSession> vecter , MinaConnect con){
		IoSession se = null ;
		while(se == null && !vecter.isEmpty()){
			se = (IoSession)vecter.firstElement() ;
			if(se != null){
				vecter.removeElementAt(0) ;
			}
			if(!con.isConnected(se)){
				se = null ;
			}
		}
		return se ;
	}
	
	/**
	 * 放新会话
	 * @param vecter
	 * @param se
	 */
	private static void putSession(Vector<IoSession> vecter , IoSession se){
		vecter.addElement(se) ;
	}
	
	/**
	 * 创建新会话
	 * @param host
	 * @param port
	 * @param connectTimeout
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private static IoSession createNewSession(String host , int port , int connectTimeout, MinaConnect con , MinaHandler handler) throws Exception{
		return con.createSession(host, port, connectTimeout , handler) ;
	}
	
	/**
	 * 得到会话池的key
	 * @param url
	 * @param port
	 * @return
	 */
	private static String getKey(String host , int port){
		return host + port ;
	}

}
