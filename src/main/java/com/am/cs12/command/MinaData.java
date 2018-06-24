package com.am.cs12.command ;

//import java.io.*;

public class MinaData {

	private Command com ;//命令
	private byte[] attachment ;//命令的附属数据，例如查询在线命令的返回结果(返回命令)时，可以把在线情况放在附属数据中
	
	public Command getCom() {
		return com;
	}
	public void setCom(Command com) {
		this.com = com;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
}
