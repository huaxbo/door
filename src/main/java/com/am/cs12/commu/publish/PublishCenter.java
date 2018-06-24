package com.am.cs12.commu.publish;

import java.util.*;

import com.am.cs12.config.PublishServerVO;
import com.am.cs12.config.ConfigCenter;

public class PublishCenter {
	
	private HashMap<String, PublishServerVO> pservers ;
	private static PublishCenter instance ;
	
	private PublishCenter(){
		
	}
	public static PublishCenter instance(){
		if(instance == null){
			instance = new PublishCenter() ;
		}
		return instance ;
	}
	
	/**
	 * 启动各个信息发布服务
	 * @param configPath 子服务配置文件相对路径
	 * @return MinaContext 返回服务上下文，以备总服务持有
	 */
	@SuppressWarnings("unchecked")
	public boolean startPublishServer()
			throws IllegalArgumentException {
		ConfigCenter cc = ConfigCenter.instance() ;
		pservers = cc.getPublishServerMap() ;
		if(pservers != null){
			Set<Map.Entry<String , PublishServerVO>> entrys = pservers.entrySet() ;
			Iterator<Map.Entry<String , PublishServerVO>> it = entrys.iterator() ;
			Map.Entry<String , PublishServerVO> entry = null ;
			String serverId = null ;
			PublishServerVO serverVo = null ; 
			while(it.hasNext()){
				entry = it.next() ;
				serverId = entry.getKey() ;
				serverVo = entry.getValue() ;
				if(serverVo.enable){
					try{
						Class c = Class.forName(serverVo.driverName);
						if (c == null) {
							throw new IllegalArgumentException("启动数据发布服务(ID=" + serverId + ")失败！");
						}
				        Object o = c.newInstance();
				        serverVo.server = (PublishServer)o ;
				        serverVo.context = serverVo.server.start(serverId, serverVo.configFilePath) ;
					}catch(Exception e){
						throw new IllegalArgumentException("启动数据发布服务(ID=" + serverId + ")失败！\n" , e);
					}
				}
			}
		}
		return true ;
	}
	
	public HashMap<String, PublishServerVO> getPservers() {
		return pservers;
	}

	public void setPservers(HashMap<String, PublishServerVO> pservers) {
		this.pservers = pservers;
	}



}
