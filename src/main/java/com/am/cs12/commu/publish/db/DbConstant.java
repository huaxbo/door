package com.am.cs12.commu.publish.db;

public class DbConstant {

	////////////////////////////
	//配置文件中的key
	public static String dbSourceName_key = "dbSourceName" ;//本地数据库存储子服务所用的数据源名称
	public static String table_key = "table" ;//数据的表名称 
	public static String getConnectTimeout_key = "getConnectTimeout" ;//如果从数据库连接池中暂时得不到连接，最大等待得到连接的时长(毫秒(1秒=1000毫秒),如果取0，则不等待，直接返回空
	
	/////////////////////////////
	//上下文中用的key
	public static String configVO_key = "configVO" ;//数据的表名称 
	

}
