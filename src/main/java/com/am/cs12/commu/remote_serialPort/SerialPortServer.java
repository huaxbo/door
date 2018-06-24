package com.am.cs12.commu.remote_serialPort;

import java.util.*;

import com.am.cs12.config.*;
import com.am.cs12.config.conf.*;

public class SerialPortServer {

	private static final int getPortDelay = 600 ;
	private static HashMap<String, SerialPortVO> serialPortMap ;
	public static HashMap<String, SerialPortChannel> serialPortChannelMap ;
	
	public SerialPortServer(){
		serialPortChannelMap = new HashMap<String, SerialPortChannel>() ;
	}
	
	/**
	 * 服务初始化
	 * @param thisServerId 本子服务ID(或都叫名称)
	 * @param configPath 子服务配置文件相对路径
	 * @throws ACException
	 */
	public SerialPortContext start() throws Exception {
		SerialPortContext ctx = new SerialPortContext();
		ctx.setObject("id", SerialPortServerConstant.serverId);

		boolean flag = true ;

		ConfigCenter cc = ConfigCenter.instance() ;
		serialPortMap = cc.getSerialPortMap() ;
		if(serialPortMap == null || serialPortMap.size() == 0){
			System.out.println("警告！串口通信服务(ID:" + SerialPortServerConstant.serverId + ")中，无可启动的串口！ ");
		}else{
			Set<Map.Entry<String, SerialPortVO>> set = serialPortMap.entrySet() ;
			Iterator<Map.Entry<String, SerialPortVO>> it = set.iterator() ;
			Map.Entry<String, SerialPortVO> entry = null ;
			SerialPortVO vo = null ;
			while(it.hasNext()){
				entry = it.next() ;
				vo = entry.getValue() ;
				SerialPortChannel ch = new SerialPortChannel(vo , SerialPortServerConstant.serverId , getPortDelay) ;
				serialPortChannelMap.put(vo.getId(), ch) ;
				if(!ch.openPort()){
					System.out.println("出错，串口通信服务(ID:" + SerialPortServerConstant.serverId + ")中，启动串口" + vo.getName() + "失败！ ");
					flag = false ;
					break ;
				}
			}
			if(flag){
				System.out.println("串口通信服务(ID:" + SerialPortServerConstant.serverId + ")已经启动 ");
			}else{
				System.out.println("串口通信服务(ID:" + SerialPortServerConstant.serverId + ")启动失败！ ");
			}
		}
		return ctx ;
	}
}
