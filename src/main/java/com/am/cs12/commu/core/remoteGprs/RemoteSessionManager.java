package com.am.cs12.commu.core.remoteGprs;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.CommandQueueForGprsSerial;
import com.am.cs12.commu.core.remoteCommand.forSM.CommandQueueForSM;

public class RemoteSessionManager {
	
	private static Logger log = LogManager.getLogger(RemoteSessionManager.class.getName()) ;
	
	//测控终端ID与测控终端网络连接会话映射
	private static HashMap<String , IoSession> rtuId_session = new HashMap<String , IoSession>() ;
	//测控终端ID与测控终端远程命令(通过GPRS或卫星网络通道发送)队列映射
	private static HashMap<String , CommandQueueForGprsSerial> rtuId_cachCommandQueueForGprsSerial = new HashMap<String , CommandQueueForGprsSerial>() ;
	//测控终端ID与上次向测控终端发送命令(通过GPRS网络通道发送)时刻映射(使用数组，目的是利用数组对不变性(但数组中的数据可变)，从而可以作为同步对象，)
	private static HashMap<String , Long[]> rtuId_sendCommandMomentForGprs = new HashMap<String , Long[]>() ;
	//测控终端ID与测控终端远程命令(通过SM短信通道发送)队列映射
	private static HashMap<String , CommandQueueForSM> rtuId_cachCommandQueueForSM = new HashMap<String , CommandQueueForSM>() ;
	
	private static RemoteSessionManager instance ;
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static RemoteSessionManager instance(){
		if(instance == null){
			instance = new RemoteSessionManager() ;
		}
		return instance ;
	}
	
	/**
	 * 私有构造方法
	 */
	private RemoteSessionManager(){
	}
	
	/**
	 * 通过RTU ID得到网络会话
	 * @param rtuId
	 * @return
	 */
	public void putSession(String rtuId , IoSession session){
		rtuId_session.put(rtuId, session) ;
	}
	/**
	 * 通过RTU ID得到网络会话
	 * @param rtuId
	 * @return
	 */
	public IoSession getSession(String rtuId){
		return rtuId_session.get(rtuId) ;
	}
	
	/**
	 * 通过RTU ID得到网络会话是否正常连接
	 * @param rtuId
	 * @return
	 */
	public boolean isConnect(String rtuId){
		IoSession se = rtuId_session.get(rtuId) ;
		if(se != null && se.isConnected()){
			return true ;
		}
		return false ;
	}
	
	/**
	 * 通过会话得到对应的RTU ID
	 * @param session
	 * @return
	 */
	public String getRtuId(IoSession session){
		Set<Map.Entry<String , IoSession>> set = rtuId_session.entrySet() ;
		Iterator<Map.Entry<String , IoSession>> it = set.iterator() ;
		Map.Entry<String , IoSession> entry = null ;
		IoSession se = null ;
		while(it.hasNext()){
			entry = it.next() ;
			se = entry.getValue() ;
			if(se == session){
				return entry.getKey() ;
			}
		}
		return null ;
	}
	/**
	 * 更新id
	 * @param newId
	 * @param oldId
	 */
	public void updateRutId(String newId,String oldId){
		IoSession se = getSession(oldId);
		if(se != null){
			putSession(newId, se);
			removeSession(oldId);
		}
	}
	/**
	 * 清除会话
	 * @param session
	 * @return
	 */
	public String removeSession(IoSession session){
		String rtuId = getRtuId(session) ;
		if(rtuId != null){
			removeSession(rtuId) ;
		}
		return rtuId;
	}
	/**
	 * 清除会话
	 * @param rtuId
	 * @return
	 */
	public void removeSession(String rtuId){
		rtuId_session.remove(rtuId) ;
	}
	
	/**
	 * 是否已经存在此会话
	 * @param session
	 * @return
	 */
	public boolean hasSession(IoSession session){
		return rtuId_session.containsValue(session) ;
	}
	
	/**
	 * 关闭网络会话
	 * @param session
	 */
	public void closeSession(IoSession session){
		session.close(true) ;
		log.info("关闭网络会话") ;
	}

	/**
	 * 关闭网络会话
	 * @param session
	 */
	public void closeSession(String rtuId, String message){
		IoSession session = getSession(rtuId) ;
		if(session != null){
			session.close(true) ;
			log.info(message + "关闭网络会话，RTU ID=" + rtuId) ;
		}
	}
	
	/**
	 * 得到测控终端对应有命令(通过GPRS网络通道)缓存队列
	 * @param rtuId
	 * @return
	 */
	public CommandQueueForGprsSerial getCachCommandQueueForGprsSerial(String rtuId){
		if(rtuId_cachCommandQueueForGprsSerial.containsKey(rtuId)){
			return rtuId_cachCommandQueueForGprsSerial.get(rtuId) ;
		}else{
			CommandQueueForGprsSerial q = new CommandQueueForGprsSerial() ;
			rtuId_cachCommandQueueForGprsSerial.put(rtuId, q) ;
			return q ;
		}
	}
	
	/**
	 * 得到测控终端对应有命令(通过SM短信通道)缓存队列
	 * @param rtuId
	 * @return
	 */
	public CommandQueueForSM getCachCommandQueueForSM(String rtuId){
		if(rtuId_cachCommandQueueForSM.containsKey(rtuId)){
			return rtuId_cachCommandQueueForSM.get(rtuId) ;
		}else{
			CommandQueueForSM q = new CommandQueueForSM() ;
			rtuId_cachCommandQueueForSM.put(rtuId, q) ;
			return q ;
		}
	}
	/**
	 * 设置上次向测控终端发送命令时刻
	 */
	public void setSendCommandMomentForGprs(String rtuId , Long moment){
		if(moment == null){
			moment = new Long(0) ;
		}
		Long[] lg = getSendCommandMomentForGprs(rtuId) ;
		lg[0] = moment ;
		rtuId_sendCommandMomentForGprs.put(rtuId, lg) ;
	}
	/**
	 * 得到上次向测控终端发送命令时刻
	 */
	public Long[] getSendCommandMomentForGprs(String rtuId){
		if(rtuId_sendCommandMomentForGprs.containsKey(rtuId)){
			return rtuId_sendCommandMomentForGprs.get(rtuId) ;
		}else{
			 Long[] moment = new Long[]{new Long(0)} ;
			rtuId_sendCommandMomentForGprs.put(rtuId, moment) ;
			return moment ;
		}
	}

	
}
