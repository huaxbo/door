package com.am.cs12.commu.core.remoteSerialPort;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.SateProtocolVO;
import com.am.cs12.config.SerialPortVO;
import com.am.cs12.commu.remote_serialPort.SerialPortHandle;
import com.am.cs12.commu.protocolSate.ActionSate;
import com.am.cs12.commu.protocolSate.CommandSate;
import com.am.cs12.commu.protocolSate.DriverSate;
import com.am.cs12.commu.protocolSate.tianHong.*;
import com.am.cs12.commu.protocolSate.tianHong.$TTCA.Param_TTCA;

public class TianHongSender {
	
	private static Logger log = LogManager.getLogger(TianHongSender.class.getName()) ;
	
	private static TianHongSender instance ;
	//将要发送的针对测控终端的一般命令集合
	private static ArrayList<Object[]> comandList;
	//将要发送的针对测控终端的确认命令集合
	private static ArrayList<Object[]> comfirmComandList;
	//将要发送的针卫星的命令集合
	private static ArrayList<Object[]> selfComandList;
	//发命令线程实例
	private static SendThread sendThread;
	
	private static Object synObj = new Object() ;
	
	private static int sendDataInterval = 60000 ;//(一分钟)
	
	/**
	 * 构造方法
	 */
	private TianHongSender(){
	}
	
	/**
	 * 得到唯一实例
	 * @return
	 */
	public static TianHongSender instance(){
		if(instance == null){
			instance = new TianHongSender() ;
			comandList = new ArrayList<Object[]>() ;
			comfirmComandList = new ArrayList<Object[]>() ;
			selfComandList = new ArrayList<Object[]>() ;
			sendThread = new SendThread();
			sendThread.start();
		}
		return instance ;
	}
	
	/**
	 * 设置要发送的确认命令
	 * @param comId
	 * @param satelliteId
	 * @param protocolName
	 * @param data
	 */
	public void setMeterConfirmCommand(String comId , String satelliteId, String protocolName , byte[] data){
		int waitingComNum = getWaitingMeterCommandCount() ;
		synchronized (comfirmComandList) {
			comfirmComandList.add(new Object[]{comId, satelliteId, protocolName , data});
		}
		if(waitingComNum == 0){
			synchronized (synObj) {
				synObj.notifyAll() ;
			}
		}
	}
	
	/**
	 * 设置要发送的一般命令
	 * @param comId
	 * @param satelliteId
	 * @param protocolName
	 * @param data
	 */
	public void setMeterCommand(String comId , String satelliteId, String protocolName , byte[] data){
		int waitingComNum = getWaitingMeterCommandCount() ;
		synchronized (comandList) {
			comandList.add(new Object[]{comId, satelliteId, protocolName , data});
		}
		if(waitingComNum == 0){
			synchronized (synObj) {
				synObj.notifyAll() ;
			}
		}
	}

	
	/**
	 * 设置要发送针对卫星自己的命令
	 * @param comId
	 * @param satelliteId
	 * @param protocolName
	 * @param data
	 */
	public void setSelfCommand(String comId, String code, String protocolName){
		int waitingComNum = getWaitingMeterCommandCount() ;
		synchronized (selfComandList) {
			selfComandList.add(new Object[]{comId, code, protocolName});
		}
		if(waitingComNum == 0){
			synchronized (synObj) {
				synObj.notifyAll() ;
			}
		}
	}

	
	/**
	 * 取出确认命令
	 */
	private static Object[] popMeterConfirmCommand() {
		synchronized (comfirmComandList) {
			if (comfirmComandList.size()==0) {
				return null;
			} else {
				return comfirmComandList.remove(0);
			}
		}
	}
	
	/**
	 * 取出一般命令
	 */
	private static Object[] popMeterCommand() {
		synchronized (comandList) {
			if (comandList.size()==0) {
				return null;
			} else {
				return comandList.remove(0);
			}
		}
	}
	
	/**
	 * 取出针对卫星自己的命令
	 */
	private static Object[] popSelfCommand() {
		synchronized (selfComandList) {
			if (selfComandList.size()==0) {
				return null;
			} else {
				return selfComandList.remove(0);
			}
		}
	}
	
	/**
	 * 得到待发命令数
	 * @return
	 */
	private static int getWaitingMeterCommandCount(){
		return comfirmComandList.size() + comandList.size() + selfComandList.size() ;
	}
	

	/**
	 * 通过串口把卫星通道数据发送出去
	 * @param com
	 */
	private static void sendMeterDataBySatellite(Object[] com) {
		try{
			String comId = (String)com[0] ;
			String satelliteId = (String)com[1] ;
			String protocolName = (String)com[2] ;
			byte[] data = (byte[])com[3] ;
			
			
			ConfigCenter cc = ConfigCenter.instance() ;
			HashMap<String, SateProtocolVO> pmap = cc.getSateProtocolMap() ;
			SateProtocolVO pvo = pmap.get(protocolName) ;
			if(pvo == null){
				log.error("严重错误，不能得到名称为" + protocolName + "的协议配置！") ;
				return ;
			}
			
			DriverTH driver = (DriverTH)initProtocolDriver(pvo.driverName , protocolName) ;
			
			CommandSate qComSate = new CommandSate() ;
			qComSate.setCode(CodeTH.$QSTA) ;

			ActionSate qAction = driver.createCommand(qComSate) ;
			if(qAction.has(ActionSate.command)){
				new SerialPortHandle().sendData(comId, driver.getCom());
			}else if(qAction.has(ActionSate.error)){
				log.error(driver.getError()) ;
			}
			
			try{
				Thread.sleep(1000L) ;
			}catch(Exception e){
				e.printStackTrace() ;
			}finally{
				Param_TTCA param = new Param_TTCA() ;
				param.setIdSate(satelliteId) ;
				param.setData(data) ;
				
				CommandSate comSate = new CommandSate() ;
				comSate.setCode(CodeTH.$TTCA) ;

				HashMap<String, Object> params = new HashMap<String, Object>() ;
				params.put(Param_TTCA.KEY, param) ;
				comSate.setParams(params) ;

				ActionSate action = driver.createCommand(comSate) ;
				if(action.has(ActionSate.command)){
					new SerialPortHandle().sendData(comId, driver.getCom());
				}else if(action.has(ActionSate.error)){
					log.error(driver.getError()) ;
				}
				
			}
			
			
		}catch(Exception e){
			log.error("通过串中发送卫星数据时出错，" + e.getMessage() , e) ;
		}finally{
			;
		}
	}

	/**
	 * 通过串口把针对卫星的命令数据发送出去
	 * @param com
	 */
	private static void sendSatelliteSelfData(Object[] com) {
		try{
			String comId = (String)com[0] ;
			String code = (String)com[1] ;
			String protocolName = (String)com[2] ;
			
			ConfigCenter cc = ConfigCenter.instance() ;
			HashMap<String, SateProtocolVO> pmap = cc.getSateProtocolMap() ;
			SateProtocolVO pvo = pmap.get(protocolName) ;
			if(pvo == null){
				log.error("严重错误，不能得到名称为" + protocolName + "的协议配置！") ;
				return ;
			}
			
			DriverTH driver = (DriverTH)initProtocolDriver(pvo.driverName , protocolName) ;

			//首先查询卫星终端状态
			CommandSate qComSate = new CommandSate() ;
			qComSate.setCode(CodeTH.$QSTA) ;
			ActionSate qAction = driver.createCommand(qComSate) ;
			if(qAction.has(ActionSate.command)){
				new SerialPortHandle().sendData(comId, driver.getCom());
			}else if(qAction.has(ActionSate.error)){
				log.error(driver.getError()) ;
			}
			
			try{
				Thread.sleep(1000L) ;
			}catch(Exception e){
				e.printStackTrace() ;
			}finally{
				//发送命令
				CommandSate comSate = new CommandSate() ;
				comSate.setCode(code) ;
				ActionSate action = driver.createCommand(comSate) ;
				if(action.has(ActionSate.command)){
					new SerialPortHandle().sendData(comId, driver.getCom());
				}else if(action.has(ActionSate.error)){
					log.error(driver.getError()) ;
				}
				
			}
		}catch(Exception e){
			log.error("通过串中发送卫星数据时出错，" + e.getMessage() , e) ;
		}finally{
			;
		}
	}

	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private static DriverSate initProtocolDriver(String clazz , String protocolName) throws IllegalArgumentException {
		try {
			Class c = Class.forName(clazz);
			if (c == null) {
				throw new IllegalArgumentException("不能实例化天鸿卫星通信协议驱动类" + clazz + "！");
			}
			return (DriverSate) c.getDeclaredConstructor(String.class).newInstance(protocolName);
		} catch (Exception e) {
			throw new IllegalArgumentException("不能实例化天鸿卫星通信协议驱动类" + clazz + "！");
		}
	}
	/**
	 * 伺服发送数据的线程
	 * 
	 * @author Administrator
	 * 
	 */
	private static class SendThread extends Thread {
		public SendThread() {
			this.setDaemon(true);
		}
		@SuppressWarnings("finally")
		public void run() {
			while (true) {
				try {
					Object[] com = popMeterConfirmCommand();
					if(com != null) {
						sendMeterDataBySatellite(com);
					}else{
						com = popMeterCommand() ;
						if(com != null) {
							sendMeterDataBySatellite(com);
						}else{
							com = popSelfCommand() ;
							if(com != null) {
								sendSatelliteSelfData(com) ;
							}
						}
					}
					if(com != null){
						ConfigCenter cc = ConfigCenter.instance() ;
						HashMap<String, SerialPortVO> map = cc.getSerialPortMap() ;
						SerialPortVO vo = map.get((String)com[0]) ;
						sendDataInterval = vo.getSendInterval() ;

						if(getWaitingMeterCommandCount() > 0){
							log.info("调用串口发送了命令，当前命令缓存队列中等待发送命令还有" + getWaitingMeterCommandCount() + "条，距下次发送命令还有" + sendDataInterval + "毫秒。") ;
						}else{
							log.info("调用串口发送了命令，当前命令缓存队列中等待发送命令还有" + getWaitingMeterCommandCount() + "条。") ;
						}
					}
					synchronized (synObj) {
						synObj.wait(sendDataInterval) ;
					}
					//Thread.sleep(sendDataInterval);
				} catch (Exception ex) {
					ex.printStackTrace() ;
				}finally{
					continue ;
				}
			}
		}
	}

}
