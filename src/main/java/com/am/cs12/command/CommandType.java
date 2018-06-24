package com.am.cs12.command;

public class CommandType {

	/**
	 * 针对通信服务器的内部命令
	 * 可实现同步，命令结果通过命令的发送网络通道返回
	 */
	public static final String innerCommand = "innerCom" ;
	
	/**
	 * 针对测控器的外部命令
	 * 只能是异步，命令结果通过相关的信息发布通道发布出去
	 */
	public static final String outerCommand = "outerCom" ;
	
}
