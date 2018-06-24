package com.am.cs12.config;



/**
 * 测控器，包括RTU数据传输单元(GPRS、CDMA或3G手机模块)和远程终端单元(测控模块)
 * 一个测控器只能有一个RTU，
 * 一个RTU可以接(透传)多个协议完全相同的RTU
 * @author liu runyu
 */
public class MeterVO {

	public String id ;
	public String phone ;//手机号
	public String satelliteId;//测控终端卫星用户ID

	public String rtuProtocol ;//rtu的协议名称
	public Integer rtuKey ;//指测控终端RTU协议中的密钥，必须配置，密钥为整数，取值范围0-9，取0为固定密码算法，如果协议无密钥，配置为0
	public Integer rtuPass ;//指测控终端RTU协议中密钥为固定密码方式时的测控终端中的RTU的密码，必须配置，密码为整数，取值范围0-4095，如果协议无密码，配置为0
	public String sateProtocol ;//卫星协议名称

	public String[] channels ;//测控终端通道，下标越小，优先级越高
	public String mainChannel ;//主通道
	public String comId ;//串口ID，对应serialPortServer.xml配置文件相关配置；

	public String[] dataTo ;//测控终端RTU数据发布方向
	public Boolean onlineAlways ;//测控终端是否常在线
	
}
