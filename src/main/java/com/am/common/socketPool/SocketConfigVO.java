package com.am.common.socketPool;

public class SocketConfigVO {

	public String poolName;//连接池名称；

	public String url;//网络连接URL； 

	public int port;//网络连接端口； 

	public int maxCount;//连接池中最大连接数； 

	public int minCount;//连接池中最大空闲连接数,或者初始创建连接数；

	public int maxCreatingNum;//网络连接创建不成功，重复尝试创建的最大次数。 

}
