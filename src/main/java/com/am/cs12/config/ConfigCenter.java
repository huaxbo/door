package com.am.cs12.config;

import java.util.HashMap;

import java.util.Map;


import com.am.common.dataSource.DbConfigVO;
import com.am.cs12.ServerConfig;
import com.am.cs12.config.conf.*;
import com.am.cs12.util.AmConstant;

public class ConfigCenter {
	
	//实现单实例
	private static ConfigCenter instance ;
	
	public static int maxMeterNumber = -1 ;//系统允许接入的最大测控终端数，这个数从lecence.xml文件中配置
	
	
	private ProtocolBaseVO protocolBaseVO ;
	private HashMap<String, RTUProtocolVO> rtuProtocolMap ;//rtu协议， 名称与协议对象映射
	private Map<String, MeterVO> id_meterMap ;
	private DbConfigVO[] dbConfigVO ;
	
	private RemoteServerVO remoteServerVO ;
	private ResetServerVO resetServerVO ;
	
	private CoreServerVO coreServerVO ;
	
	private AmLogVO amLogVO ;
	

	/**
	 * 得到单实例
	 * @return
	 */
	public static ConfigCenter instance(){
		if(ConfigCenter.instance == null){
			ConfigCenter.instance = new ConfigCenter() ; 
		}
		return ConfigCenter.instance ;
	}
	
	/**
	 * 构造方法
	 */
	private ConfigCenter(){
	}
	
	/**
	 * 初始化中心执行初始化操作
	 * @return
	 */
	public boolean init(){
		boolean flag = false ;
		if(flag = this.initProtocolBaseConfig()){
			System.out.println("基础协议配置初始化完毕");
		}
		if(flag && ServerConfig.remoteServerEnable){
			if(flag = this.initRTUProtocolConfig()){
				System.out.println("RTU协议配置初始化完毕");
			}
		}
		if(flag && ServerConfig.remoteServerEnable){
			if(flag = this.initMeterConfig()){
				System.out.println("测控器配置初始化完毕");
			}
			this.initRemoteServerConfig();
		}
		
		if(flag = this.initCoreServerConfig()){
			System.out.println("核心调度服务初始化完毕");
		}
		if(flag = this.initAmLogConfig()){
			System.out.println("测控终端日志配置初始化完毕");
			flag = true ;
		}
			
		return flag ;
	}
	

	
	/**
	 * 初始化协议基础配置
	 * @return
	 */
	private boolean initProtocolBaseConfig(){
		try {
			ProtocolBaseConfig pbc = new ProtocolBaseConfig() ;
			String path = pbc.createDom(AmConstant.pbcConfigFilePath) ;
			protocolBaseVO = pbc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化协议基础配置出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 初始化RTU协议配置
	 * @return
	 */
	private boolean initRTUProtocolConfig(){
		try {
			ProtocolRTUConfig prc = new ProtocolRTUConfig() ;
			String path = prc.createDom(AmConstant.RTUConfigFilePath) ;
			rtuProtocolMap = prc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化RTU和RTU协议配置(protocols.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 初始化测控器配置
	 * @return
	 */
	private boolean initMeterConfig(){
		try {
			MeterConfig mc = new MeterConfig() ;
			String path = mc.createDom(AmConstant.meterConfigFilePath) ;
			id_meterMap = mc.parseOptions(path) ;
			if(id_meterMap.size() > maxMeterNumber){
				System.out.println("严重错误，通信服务器接入测控终数超过lecence允许数据量 !");
				return false ;
			}
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化测控器配置(meters.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 初始化远程服务配置
	 * @return
	 */
	private boolean initRemoteServerConfig(){
		try {
			RemoteServerConfig rsc = new RemoteServerConfig() ;
			String path = rsc.createDom(AmConstant.remoteServerConfigFilePath) ;
			remoteServerVO = rsc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化测控器配置(remoteServer.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 初始化测控器配置
	 * @return
	 */
	private boolean initCoreServerConfig(){
		try {
			CoreServerConfig csc = new CoreServerConfig() ;
			String path = csc.createDom(AmConstant.coreServerConfigFilePath) ;
			coreServerVO = csc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化测控器配置(coreServer.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 初始化测控终端日志
	 * @return
	 */
	private boolean initAmLogConfig(){
		try {
			AmLogConfig csc = new AmLogConfig() ;
			String path = csc.createDom(AmConstant.amLogConfigFilePath) ;
			amLogVO = csc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化测控终端日志配置(amLog.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
		
	public ProtocolBaseVO getProtocolBaseVO() {
		return protocolBaseVO;
	}
	public HashMap<String, RTUProtocolVO> getRtuProtocolMap() {
		return rtuProtocolMap;
	}
	
	public DbConfigVO[] getDbConfigVO() {
		return dbConfigVO;
	}
	public RemoteServerVO getRemoteServerVO() {
		return remoteServerVO;
	}
	public CoreServerVO getCoreServerVO() {
		return coreServerVO;
	}
	public Map<String, MeterVO> getId_meterMap() {
		return id_meterMap;
	}
	public void setId_meterMap(Map<String, MeterVO> idMeterMap) {
		id_meterMap = idMeterMap;
	}
	public AmLogVO getAmLogVO() {
		return amLogVO;
	}
	public ResetServerVO getResetServerVO() {
		return resetServerVO;
	}

}
