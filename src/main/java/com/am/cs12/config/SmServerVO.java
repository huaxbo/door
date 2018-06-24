package com.am.cs12.config;

public class SmServerVO {
	public String id ;//服务ID
	// 调试阶段，未接短信猫，以备业务系统感觉到发短信成功:设置为true  
	// 正式运行阶段，设置为false ;
	public Boolean debugForNoModel ;
	public String device ;//短信猫所在的串口
	public Integer baudrate ;//串口波特率
	
	public String modelType ;//短信猫类型，目前只支持两类 人大金仓(jincang)和金笛(jindi) 
	public String smId ;//短信猫ID (金笛专用)
	public String factory ;//短信猫芯片厂家(法国Wavecom或中国Huawei) (金笛专用)
	public String sn ;//短信猫序列号 (金仓专用)
	
	
	public String[] codeOnlyBySm ;//只能通过短信通道发送的命令功能码
	public String[] codeEnableBySm ;//可以通过短信通道发送的命令功能码
	
	public SmServerVO(){
		
	}

}
