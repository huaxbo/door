package com.am.cs12.config;


public class SateProtocolVO {
	/**
	 * 协议名称
	 */
	public String name ;
	/**
	 * 协议驱动
	 */
	public String driverName ;
	
	/**
	 * 是否开启卫星授时功能 ，开启true,关闭false
	 */
	public boolean enableGrantTime ;
	/**
	 * 校时启动时刻--小时(取值范围0到23)
	 */
	public int grantTimeStartHour ;
	/**
	 * 校时启动时刻--分钟(取值范围10到50)
	 */
	public int grantTimeStartMinute ;
	
	
	/**
	 * 卫星时钟与本地时钟误差达到多少分钟时进行校时(取值范围1到5)
	 */
	public int grantTimeByDeviate ;
	
}
