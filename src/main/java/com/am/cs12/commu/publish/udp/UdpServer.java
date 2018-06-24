package com.am.cs12.commu.publish.udp;

import java.util.List;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.publish.PublishServer;
import com.am.cs12.commu.publish.PublishServerContext;

public class UdpServer  implements PublishServer{

	@Override
	public PublishServerContext start(String thisServerId, String configPath)
			throws IllegalArgumentException {
		System.out.println("UDP网络数据发布服务(ID:" + thisServerId + ")已经启动") ;
		return new UdpContext() ;
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 使能继续处理数据
	 */
	@Override
	public void enable() {
		
	}
	/**
	 * 使不能处理数据
	 */
	@Override
	public void disable(){
		
	}
	/**
	 * 放入数据，以供发布
	 * @param commandId 命令ID，如果是主动上报数据，则其为空
	 * @param data
	 */
	@Override
	public void putData(String commandId ,Data d)throws Exception {
	}

	/**
	 * 放入数据，以供发布
	 * @param commandId 命令ID，如果是主动上报数据，则其为空
	 * @param list
	 */
	@Override
	public void putData(String commandId , List<Data> list)throws Exception{
		
	}


}

