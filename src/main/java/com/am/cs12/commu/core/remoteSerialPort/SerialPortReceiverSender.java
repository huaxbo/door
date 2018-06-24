package com.am.cs12.commu.core.remoteSerialPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.config.*;
import com.am.cs12.util.AmConstant;
import com.am.cs12.commu.core.despatchReceive.forRemote.DespatchForSate;

public class SerialPortReceiverSender {
	
	private static Logger log = LogManager.getLogger(SerialPortReceiverSender.class.getName()) ;

	/**
	 * 串口通道发送确认命令
	 * @param id
	 * @param command
	 */
	public void sendMeterConfirmCommand(String id, byte[] command){
		MeterHelp h = new MeterHelp() ;
		MeterVO vo = h.getMeter(id) ;
		String comId = vo.comId ;
		if(comId == null){
			log.error("严重错误，选择了卫星通道发送命令，但未得到测控终端所对应的卫星通道ID！") ;
		}else if(comId.equals(AmConstant.tianHong_COM)){
			TianHongSender.instance().setMeterConfirmCommand(comId, vo.satelliteId, vo.sateProtocol, command) ;
		}else{
			log.error("严重错误，当前通信服务器实现只支持卫星串口通道" + AmConstant.tianHong_COM + "，此次通信串口通道" + comId + "不支持") ;
		}
	}
	
	/**
	 * 串口通道发送一般命令
	 * @param id
	 * @param command
	 */
	public void sendMeterCommand(String id, byte[] command){
		MeterHelp h = new MeterHelp() ;
		MeterVO vo = h.getMeter(id) ;
		String comId = vo.comId ;
		if(comId == null){
			log.error("严重错误，选择了卫星通道发送命令，但未得到测控终端所对应的卫星通道ID！") ;
		}else if(comId.equals(AmConstant.tianHong_COM)){
			TianHongSender.instance().setMeterCommand(comId, vo.satelliteId, vo.sateProtocol, command) ;
		}else{
			log.error("严重错误，当前通信服务器实现只支持卫星串口通道" + AmConstant.tianHong_COM + "，此次通信串口通道" + comId + "不支持") ;
		}
	}
	
	/**
	 * 从串口收到远程测控终端数据
	 * @param satelliteId
	 * @param data
	 */
	public void receiverMeterDataBySatellite(String satelliteId , byte[] data){
		new DespatchForSate().receiveData(satelliteId, data) ;
	}
}
