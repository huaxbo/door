package com.am.cs12.config.conf ;

public class LocalServerConstant {
	/**
	 * key - Mina服务ID
	 */
	public static final String MINASERVERID = "id" ;
	
	/**
	 *key - Mina通信服务对外提供的端口号
	 */
	public static final String MINASERVERPORT = "port" ;
	
	/**
	 *key - Mina通信服务NioSocketAcceptor所应用的IoProcessor用数量，默认是CUP数+1
	 */
	public static final String MINAPROCESSORS = "processors" ;
	
	/**
	 *key - Mina通信服务网络联接读写空闲操作时长，超过时长认为通信结速(秒)
	 */
	public static final String MINAIDLE = "idle" ;
	
	
	/**
	 * key - 客户端与本服务器端Mina联接是否为长联接，如果为长联接则配置为true，否则配置为false。
	 */
	public static final String MINASERVERCONNECTFOREVER = "connectForever" ;

}
