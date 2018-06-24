package com.am.cs12.config.conf;

public class SmServerConstant {
	public static String ID = "id" ;
	public static String DEBUGFORNOMODEL = "debugForNoModel" ;
	public static String DEVICE = "device" ;
	public static String BAUDRATE = "baudrate" ;
	public static String MODELTYPE = "modelType" ;//短信猫类型，目前只支持两类 人大金仓(jincang)和金笛(jindi) 
	public static String SMID = "smId" ;//短信猫ID (金笛专用)
	public static String FACTORY = "factory" ;//短信猫芯片厂家(法国Wavecom或中国Huawei) (金笛专用)
	public static String SN  = "sn" ;//短信猫序列号 (金仓专用)
	
	
	//只能通过短信通道发送的命令功能码,
	//GPRS通道不能发送该命令，例如短信唤醒功能
	public static String CODEONLYBYSM = "codeOnlyBySm" ; 
	//可以通过短信通道发送的命令功能码 
	public static String CODEENABLEBYSM = "codeEnableBySm" ; 

}
