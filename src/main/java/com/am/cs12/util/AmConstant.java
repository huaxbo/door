package com.am.cs12.util;

public class AmConstant {
	
	public static final Integer MAX_DATA_SIZE = 1100 ;//上报数据的最大长度，以进行完整性检查，即断包粘包检查
	
	public static final Integer rtuDefaultPasswordKey = 0 ;
	public static final Integer rtuDefaultPassword = 0 ;
	public static final Integer subRtuDefaultPasswordKey = 0 ;
	public static final Integer subRtuDefaultPassword = 0 ;
	
	public static final Integer workModel_int_0 = 0 ;
	public static final Integer workModel_int_1 = 1 ;
	public static final Integer workModel_int_2 = 2 ;
	public static final Integer workModel_int_3 = 3 ;
	public static final String workModel_str_0 = "兼容" ;
	public static final String workModel_str_1 = "自报" ;
	public static final String workModel_str_2 = "查询/应答" ;
	public static final String workModel_str_3 = "调试/维修" ;
	
	
	public static final String protocolType_satellite = "sate" ;//协议类型---卫星协议
	

	public static final String channel_serialPort = "serialPort" ;//串口通道(卫星通过串口)
	public static final String channel_gprs = "gprs" ;//GPRS通道
//	public static final String channel_sm = "sm" ;
	
	public static final String tianHong_protocolName = "tianHong" ;//神州天鸿卫星协议名称
	public static final String tianHong_COM = "tianHong_COM" ;//神州天鸿卫星所接串口通道ID
	
	//卫星4.0   start---------
	public static final String beiDou_protocolName = "beiDou" ;//北斗4.0协议
	public static final String beiDou_COM = "beiDou_COM" ; //北斗4.0所接串口通讯
	
	
	public static final String RemoteTrigger = "remoteTrigger" ;//远程测控终端上报数据事件生成的触发
	public static final String LocalTrigger = "localTrigger" ;//缓存命令定时任务及中心系统下发命令事件生成的触发

	private static final String configFileBasePath = "/config/" ;

	public static final String log4jConfigFilePath = configFileBasePath + "log4j.properties" ;
	public static final String lecenceFilePath = configFileBasePath + "licence.xml" ;
	

	
	public static final String pbcConfigFilePath = configFileBasePath + "protocolsBase.xml" ;
	public static final String RTUConfigFilePath = configFileBasePath + "protocols.xml" ;
	public static final String sateConfigFilePath = configFileBasePath + "protocols.xml" ;
	public static final String publishServerConfigFilePath = configFileBasePath + "publishServers.xml" ;
	public static final String dataSourceConfigFilePath = configFileBasePath + "dataSource.xml" ;
	public static final String remoteServerConfigFilePath = configFileBasePath + "remoteServer.xml" ;
	public static final String localServerConfigFilePath = configFileBasePath + "localServer.xml" ;
	public static final String smServerConfigFilePath = configFileBasePath + "smServer.xml" ;
	public static final String serialPortServerConfigFilePath = configFileBasePath + "serialPortServer.xml" ;
	public static final String resetServerConfigFilePath = configFileBasePath + "resetServer.xml" ;
	public static final String coreServerConfigFilePath = configFileBasePath + "coreServer.xml" ;
	public static final String amLogConfigFilePath = configFileBasePath + "amLog.xml" ;
	public static final String pictureConfigFilePath = configFileBasePath + "picture.xml" ;

	public static final String meterConfigFilePath = configFileBasePath + "meters.xml" ;
	public static final String meterConfigFileTimeFilePath = configFileBasePath + "system/meterConfigFileTime.am" ;
	public static final String meterStatusFilePath = configFileBasePath + "system/meterStatus.xml" ;
		

	public static final String indexPath = "index.html" ;
	public static final String mainPath = "main.html" ;
}
