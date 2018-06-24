package com.am.cs12.commu.protocolSate;



public abstract class DriverSate {
	
	protected String protocolName ;//协议名称

	/**
	 * 带参数的构造方法，参数为协议名称
	 * @param protocolName 协议名称
	 */
	public DriverSate(String protocolName){
		this.protocolName = protocolName ;
	}
	/**
	 * 分析卫星终端发来的数据
	 * @param data
	 * @param dataHex
	 * @return
	 */
	public abstract ActionSate analyseData(byte[] data, String dataHex) ;
	/**
	 * 构造发向卫星终端的命令
	 * @param com 命令
	 * @return
	 */
	public abstract ActionSate createCommand(CommandSate com);
	
	/**
	 * 启动授时
	 */
	public abstract void startGrantTime() ;

}
