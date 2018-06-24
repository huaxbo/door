package com.am.cs12.config;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


import com.am.common.dataSource.DbConfigVO;
import com.am.cs12.ServerConfig;
import com.am.cs12.config.conf.*;
import com.am.common.timerTask.ACSchedularJob;
import com.am.cs12.config.meter.monitor.*;
import com.am.cs12.util.AmConstant;

public class ConfigCenter {
	
	private static final Logger log = LogManager.getLogger(ConfigCenter.class.getName()) ;
	
	//实现单实例
	private static ConfigCenter instance ;
	
	public static int maxMeterNumber = -1 ;//系统允许接入的最大测控终端数，这个数从lecence.xml文件中配置
	
	
	private ProtocolBaseVO protocolBaseVO ;
	private HashMap<String, RTUProtocolVO> rtuProtocolMap ;//rtu协议， 名称与协议对象映射
	private HashMap<String, SateProtocolVO> sateProtocolMap ;//卫星协议， 名称与协议对象映射
	private HashMap<String, PublishServerVO> publishServerMap ;
	private Map<String, MeterVO> id_meterMap ;
	private DbConfigVO[] dbConfigVO ;
	private HashMap<String, SerialPortVO> serialPortMap ;//串口配置
	
	private RemoteServerVO remoteServerVO ;
	private LocalServerVO localServerVO ;
	private SmServerVO smServerVO ;
	private ResetServerVO resetServerVO ;
	
	private CoreServerVO coreServerVO ;
	
	private AmLogVO amLogVO ;
	
	private PictureVO pictureVO ;


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
		if(flag && ServerConfig.serialPortServerEnable && ServerConfig.satelliteEnable){
			if(flag = this.initSateProtocolConfig()){
				System.out.println("卫星协议配置初始化完毕");
			}
		}
		if(flag && ServerConfig.publishServerEnable){
			if(flag = this.initPublishServerConfig()){
				System.out.println("本地服务配置初始化完毕");
			}
		}
		if(flag && ServerConfig.dataSourceServerEnable){
			if(flag = this.initDataSourceConfig()){
				System.out.println("数据源配置初始化完毕");
			}
		}
		if(flag && ServerConfig.remoteServerEnable){
			if(flag = this.initRemoteServerConfig()){
				System.out.println("远程服务初始化完毕");
			}
		}
		if(flag && ServerConfig.localServerEnable){
			if(flag = this.initLocalServerConfig()){
				System.out.println("本地服务初始化完毕");
			}
		}
		if(flag && ServerConfig.smServerEnable){
			if(flag = this.initSmServerConfig()){
				System.out.println("短信服务初始化完毕");
			}
		}
		if(flag && ServerConfig.serialPortServerEnable){
			if(flag = this.initSerialPortServerConfig()){
				System.out.println("串口通信服务初始化完毕");
			}
		}
		
		if(flag && (ServerConfig.remoteServerEnable || ServerConfig.satelliteEnable)){
			if(flag = this.initMeterConfig()){
				System.out.println("测控器配置初始化完毕");
			}
		}
		
		if(flag = this.initCoreServerConfig()){
			System.out.println("核心调度服务初始化完毕");
		}
		if(flag = this.initAmLogConfig()){
			System.out.println("测控终端日志配置初始化完毕");
			flag = true ;
		}
		if(ServerConfig.pictureEnable){
			if(flag = this.initPictureConfig()){
				System.out.println("测控终端图像配置初始化完毕");
				flag = true ;
			}
		}
				
				
		/*if(flag){
			try {
				new ACSchedularJob().addSecondlyJob(
						MonitorConstant.monitorXmljob , 
						MonitorConstant.monitorXmljobGroup , 
						MonitorMeterConfig.class , 
						null ,
						MonitorConstant.monitorDealy , 
						MonitorConstant.monitorInterval,
						-1) ;
			} catch (Exception e) {
				flag = false ;
				log.error("启动meter配置文件的监视器失败:" + e.getMessage() , e) ;
			}
		}*/

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
	 * 初始化卫星协议配置
	 * @return
	 */
	private boolean initSateProtocolConfig(){
		try {
			ProtocolSateConfig prc = new ProtocolSateConfig() ;
			String path = prc.createDom(AmConstant.sateConfigFilePath) ;
			sateProtocolMap = prc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化卫星协议配置(protocols.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 初始化本地服务配置
	 * @return
	 */
	private boolean initPublishServerConfig(){
		try {
			PublishServerConfig lsc = new PublishServerConfig() ;
			String path = lsc.createDom(AmConstant.publishServerConfigFilePath) ;
			publishServerMap = lsc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化本地服务配置(publishServers.xml)出错 !");
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
	 * 初始化数据源配置
	 * @return
	 */
	private boolean initDataSourceConfig(){
		try {
			DataSourceConfig dsc = new DataSourceConfig() ;
			String path = dsc.createDom(AmConstant.dataSourceConfigFilePath) ;
			dbConfigVO = dsc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化数据源配置(dataSource.xml)出错 !");
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
	 * 初始化本地服务配置
	 * @return
	 */
	private boolean initLocalServerConfig(){
		try {
			LocalServerConfig lsc = new LocalServerConfig() ;
			String path = lsc.createDom(AmConstant.localServerConfigFilePath) ;
			localServerVO = lsc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化本地服务配置localServer.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 初始化短信服务配置
	 * @return
	 */
	private boolean initSmServerConfig(){
		try {
			SmServerConfig sc = new SmServerConfig() ;
			String path = sc.createDom(AmConstant.smServerConfigFilePath) ;
			smServerVO = sc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化短信服务配置(smServer.xml)出错 !");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 初始化串口通信服务配置
	 * @return
	 */
	private boolean initSerialPortServerConfig(){
		try {
			SerialPortServerConfig sc = new SerialPortServerConfig() ;
			String path = sc.createDom(AmConstant.serialPortServerConfigFilePath) ;
			serialPortMap = sc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化串口通信服务配置(serialPortServer.xml)出错 !");
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
	
	/**
	 * 初始化测控终端日志
	 * @return
	 */
	private boolean initPictureConfig(){
		try {
			PictureConfig pc = new PictureConfig() ;
			String path = pc.createDom(AmConstant.pictureConfigFilePath) ;
			pictureVO = pc.parseOptions(path) ;
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println("通信服务器启动时，初始化测控终端图像配置(picture.xml)出错 !");
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
	
	public HashMap<String, SateProtocolVO> getSateProtocolMap() {
		return sateProtocolMap;
	}

	public HashMap<String, PublishServerVO> getPublishServerMap() {
		return publishServerMap;
	}
	public DbConfigVO[] getDbConfigVO() {
		return dbConfigVO;
	}
	public LocalServerVO getLocalServerVO() {
		return localServerVO;
	}
	public RemoteServerVO getRemoteServerVO() {
		return remoteServerVO;
	}
	public SmServerVO getSmServerVO() {
		return smServerVO;
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
	public PictureVO getPictureVO() {
		return pictureVO;
	}
	public HashMap<String, SerialPortVO> getSerialPortMap() {
		return serialPortMap;
	}
	public ResetServerVO getResetServerVO() {
		return resetServerVO;
	}

}
