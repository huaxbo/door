package com.am.cs12.commu.publish;

import java.util.*;

import com.am.cs12.commu.protocol.Data;

public interface PublishServer {
	/**
	 * 启动服务 
	 * @param thisServerId 本子服务ID(或都叫名称)
	 * @param configPath 子服务配置文件相对路径
	 * @return MinaContext 返回服务上下文，以备总服务持有
	 */
	public PublishServerContext start(String thisServerId, String configPath)
			throws Exception ;
	
	/**
	 * 停止服务
	 */
	public void stop() ;
	
	/**
	 * 使能继续处理数据
	 */
	public void enable() ;
	
	/**
	 * 使不能处理数据
	 */
	public void disable() ;
	/**
	 * 放入数据，以供发布
	 * @param commandId 命令ID，如果是主动上报数据，则其为空
	 * @param data
	 */
	public void putData(String commandId , Data d)throws Exception  ;
	/**
	 * 放入数据，以供发布
	 * @param commandId 命令ID，如果是主动上报数据，则其为空
	 * @param data
	 */
	public void putData(String commandId ,List<Data> list)throws Exception  ;
	
	
}
