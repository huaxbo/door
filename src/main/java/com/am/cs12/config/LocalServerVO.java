package com.am.cs12.config;

public class LocalServerVO {
	public String id ;//服务ID
	public Integer port ;//对外提供服务的端口号
	public Integer processors ;//NioSocketAcceptor所应用的IoProcessor用数量，默认是CUP数+1
	public Integer idle ;//网络联接读写空闲时长，超过时长认为通信结速(秒)
	public Boolean connectForever ;//客户端与本服务器端TCP联接是否为长联接，如果为长联接则配置为true，否则配置为false ;

}
