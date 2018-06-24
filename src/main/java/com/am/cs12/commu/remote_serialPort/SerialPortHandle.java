package com.am.cs12.commu.remote_serialPort;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.SerialPortVO;
import com.am.cs12.util.AmConstant;
import com.am.cs12.commu.core.remoteSerialPort.BeiDouReceiver;
import com.am.cs12.commu.core.remoteSerialPort.TianHongReceiver;

public class SerialPortHandle {
	private static Logger log = LogManager.getLogger(SerialPortHandle.class);
	
	
	/**
	 * 从串口收到数据
	 * @param threadNum
	 * @param bs
	 */
	public void receiveData(String comId , byte[] bs) {
		if (bs == null) {
			log.error("出错，从串口(ID:" + comId + ")得到远端的数据为null!"); 
			return;
		}
		if(comId == null){
			log.error("出错，分发串口数据时，comId为空!"); 
			return;
		}
		ConfigCenter cc = ConfigCenter.instance() ;
		HashMap<String, SerialPortVO> map = cc.getSerialPortMap() ;
		if(map == null){
			log.error("出错，未能得到串口配置数据!"); 
			return;
		}
		SerialPortVO vo = map.get(comId) ;
		if(vo == null){
			log.error("出错，未能得到ID为" + comId + "串口配置数据!"); 
			return;
		}
		String pname = vo.getProtocolName() ;
		if(pname == null){
			log.error("出错，未能得到ID为" + comId + "串口所对应的协议配置!"); 
			return;
		}
		if(pname.equals(AmConstant.tianHong_protocolName)){
			TianHongReceiver thr = new TianHongReceiver() ;
			thr.receiveData(vo.getId(), pname, bs) ;
		}
		else if(pname.equals(AmConstant.beiDou_protocolName)){ //添加北斗4.0协议解析部分
			BeiDouReceiver bdr = new BeiDouReceiver() ;
			bdr.receiveData(vo.getId(), pname, bs) ;
		}
		else{
			log.error("出错，当前系统不支持串口(ID:" + comId + ")对应的协议(" + pname + ")!"); 
			return;
		}
		
	}

	/**
	 * 向串口发数据
	 * @param threadNum
	 * @param bs
	 */
	public void sendData(String comId , byte[] bs) {
		if (bs == null) {
			log.info("出错，向串口(ID:" + comId + ")发送数据为null!"); 
			return;
		}
		
		SerialPortChannel ch = SerialPortServer.serialPortChannelMap.get(comId) ;
		if(ch == null){
			log.info("出错，不能得到串口(ID:" + comId + ")的通道!"); 
			return;
		}else{
			ch.write(bs) ;
		}
	}

}
