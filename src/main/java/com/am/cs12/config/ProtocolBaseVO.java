package com.am.cs12.config;

/**
 * 协议基础配置对象类
 *
 */
public class ProtocolBaseVO {
	//测控器上报数据的头部最小长度，不同协议不同，选择一个最大作为最小值，以全包含，
	//设置该属性以备进行完整性检查，即断包与粘包检查
	public int headMinLength ;
	
	//允许重发一个命令的最大次数，
	//即如果通信服务器下发命令后测控器未回复命令结果，通信服务器将再次发送此命令，
	//直到收到命令回复结果，或达到最大命令发送次数 ，
	//发送命令的最大次数包括第一次发送及后续再次发送的次数。
	public int maxSendCommandTimes ;
	//重复发命令的时间间隔(秒)，也就是最大等待命令回数的时间间隔，
	//如果大于这个时间，就认为命令没有回数。
	//重复发命令的时间间隔最小不能小于30秒
	public int sendCommandInterval ;
	//命令超时时长(秒)
	//从命令最后一次发出后开始记时，超过配置的时长后，命令
	//将从命令缓存中被强制清除
	public int commandTimeout ;

	//下送确认命令与其他命令(包括确认命令)间的时间间隔(秒钟)
	public int confirmCommandInterval ;
	
	//最大连续命令失败次数(单个命令多次重发仍算作一次命令)，达到这个数，即认为GPRS不在线 
	//当取值为-1时，表示不进行最大连续命令失败次数控制，即不论失败多少次，总认为GPRS在线
	public int maxContinueFailCommandTimes ;
	
	//测控终端网络联接最小无上传数据的时长，达到这个时长，即认为测控终端已经不在线。
	//这个时间要根据具体项目而定，不同项目所在地区不同，测控终端的最大心跳间隔时长也不同，
	//测控终端的最大心跳间隔时长，也就是允许的测控终端网络联接最小无上传数据时间,
	//所以此值应该大于所有测控终端中最大心跳间隔时长。
	//单位(分钟)，取值范围(1到65)(取60以上，目的是：如果测控终端无心跳，则可以用整点上报数据作最小上传数据时间间隔)
	public int minMeterConnectIdleInterval ;
	
	//通过"测控终端网络联接最小无上传数据的时长"规则，判断测控终端不在线时，是否关闭网络连接
	//配置值只能为true或false
	//true:关闭
	//false:不关闭
	public boolean checkConnectOffCloseSocketForIdleInterval ;
	
	//通过"最大连续命令失败次数"规则，判断测控终端不在线时，是否关闭网络连接
	//配置值只能为true或false
	//true:关闭
	//false:不关闭
	public boolean checkConnectOffCloseSocketForContinueFailCommandTimes ;
	
	
 	//通信服务器自动较时测控终端时钟，
 	//测控终端数据中如果包含时钟，则进行时钟比对，
 	//如果时钟相差在最大允许误差范围内，则不进行较时，如果在大允许误差范围外，则进行较时。
 	//因为雨量计在8时时进行日数据设置，所有在测控终端8时时不能过行较时
	//是否开启较时功能
	public boolean synchronizeClockEnable ;
	//拒绝较时的测控器所在小时(一般为8时)
	public int synchronizeClockdeny ;
	//最大允许误差(分钟) 
	public int synchronizeClockDifference ;
	
	//因为测控终端RTU可能会无故转变工作模式，使得其不能按预期上报数据。
	//当发生这种工作模式跳变时，对RTU进行复位操作，会恢复原来的工作模式，从而可以按预期上报数据。
	//当人为发命令更改工作模式，也会引起这种复位操作，但不影响命令结果，限仍能按命令正确地改变工作模式
	// 是否开工作模式转变时进行复位测控终端功能   true为启动，false为不启动
	public boolean resetRtuWhileModelChange ;

	
	private static ProtocolBaseVO instance ;
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static ProtocolBaseVO instance(){
		if(instance == null){
			instance = new ProtocolBaseVO() ;
		}
		return instance ;
	}
	
	private ProtocolBaseVO(){
		maxSendCommandTimes = 2 ;
		sendCommandInterval = 60 ;
		maxContinueFailCommandTimes = 3 ;
		confirmCommandInterval = 5 ;
		minMeterConnectIdleInterval = 10 ;
		checkConnectOffCloseSocketForIdleInterval = true ;
		checkConnectOffCloseSocketForContinueFailCommandTimes = true ;
		synchronizeClockEnable = false ;
		synchronizeClockdeny = 8 ;
		synchronizeClockDifference = 5 ;
	}

}
