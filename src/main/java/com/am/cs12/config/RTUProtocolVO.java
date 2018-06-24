package com.am.cs12.config;


public class RTUProtocolVO {
	/**
	 * 协议名称
	 */
	public String name ;
	/**
	 * 协议驱动
	 */
	public String driverName ;
//	protected DriverRtu dirver ;
	/**
	 * 上线匹配内容的正则表达式
	 */
	public String onLine ;

	/**
	 * 指在上线数据字节数组中，ID编码类型，分别有字符串(ASCII)、整数(INT)、BCD编码
	 */
	public String idType ;
	/**
	 * 指在上线字数据节数组中，ID编码起始位(包含)
	 */
	public int idFrom ;
	/**
	 * 指在上线字数据节数组中，ID编码终止位(包含)
	 */
	public int idEnd ;
	/**
	 * 带有长度的数据头的 正则表达式,以备完整性检查及处理断包粘包问题
	 */
	public String complete ;
	/**
	 * 指在上线数据字节数组中，数据长度字节编码类型，分别有字符串(ASCII)、整数(INT)、BCD编码
	 */
	public String lenType ;
	/**
	 * 指在上线字数据节数组中，数据长度字节编码起始位(包含)
	 */
	public int lenFrom ;
	/**
	 * 指在上线字数据节数组中，数据长度字节编码终止位(包含)
	 */
	public int lenEnd ;
	/**
	 * 属性(附加数据长度) ：上报一帧数据长度=标识的数据长度 +附加数据长度
	 */
	public Integer extraLen ;
	/**
	 * 通信服务器发送心跳到远端测控器(1)  远端发送心跳到通信服务器(0)  两端都不需要心跳(9)
	 */
	public int sendHeartBeat ;
	/**
	 * 心跳标识字符串 没有发心跳或在协议实现类中内含心跳字符串，心跳标识字符串为空串
	 */
	public String heartBeatString ;
	/**
	 * 心跳时间间隔(秒)
	 */
	public int heartBeatInterval ;
	/**
	 * 接收心跳方是否回复心跳 , 回复=1 , 不回复=0 , 无意义=-1由协议内部实现,此处配置无意义
	 */
	public int returnHeartBeat ;
	
	
}
