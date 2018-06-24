package com.am.cs12.commu.protocol;

import com.am.cs12.command.Command;

public abstract class DriverMeter {
	
	//发向测控器的命令
	protected byte[] remoteData;
	
	//发向测控器的字符串形式命令数据，主要指短信命令，当前只有wakeup字符串
	protected String remoteDataStr ;

	//下发命令时设置的ID，或上报数据中的ID 
	protected String id ;
	
	//命令的功能码
	protected String commandCode ;

	//数据中的功能码
	protected String dataCode ;
	
	//更改 ID命令应答后，应答数据中的新的 ID，
	protected String newId ;

	//测控器工作模式
	protected Integer meterWorkModel ;

	//错误消息
	protected String error;
	
	//协议名称
	protected String protocolName ;

	/**
	 * 带参数的构造方法，参数为协议名称
	 * @param protocolName 协议名称
	 */
	public DriverMeter(String protocolName){
		this.protocolName = protocolName ;
	}
	
	/**
	 * 分析RTU数据
	 * @param id
	 * @param data
	 * @param dataHex
	 * @return
	 */
	public abstract Action analyseData(String id, byte[] data, String dataHex) ;
	/**
	 * 构造针对RTU(测控终端)的命令
	 * @param com 命令
	 * @param key_password RTU的密钥与密码
	 * @return
	 */
	public abstract Action createCommand(Command com, Integer[] key_password);
	
	/**
	 * 直接创建设置时钟命令
	 * @param id  
	 * @param key_password RTU的密钥与密码
	 * @return
	 */
	public abstract byte[] createSetClockCommandDirect(String id, Integer[] key_password);
	
	/**
	 * 得到上报数据中的功能码，
	 * @return
	 */
	public abstract String getDataCode();
	
	/**
	 * 得到命令的功能码，
	 * @return
	 */
	public abstract String getCommandCode();

	/**
	 * 得到发向远程测控器的命令数据
	 * @return
	 */
	public abstract byte[] getRemoteData();
	
	/**
	 * 得到发向远程测控器的字符串形式命令数据，主要指短信命令，当前只有wakeup字符串
	 * @return
	 */
	public abstract String getRemoteDataStr();

	/**
	 * 得到下发命令时设置的ID，或上报数据中的ID 
	 * @return
	 */
	public abstract String getId() ;
	
	/**
	 * 得到修改后的测控终端ID
	 * @return
	 */
	public String getNewId() {
		return newId;
	}

	/**
	 * 得到要发向中心业务系统的数据
	 * @return
	 */
	public abstract Data getCenterData();

	/**
	 * 得到测控器工作模式
	 * @return
	 */
	public abstract Integer getMeterWorkModel() ;
	
	/**
	 * 错误消息
	 * @return
	 */
	public abstract String getError() ;

}
