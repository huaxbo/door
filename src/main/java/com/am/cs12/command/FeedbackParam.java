package com.am.cs12.command;

public class FeedbackParam {
	
	public static final String KEY = FeedbackParam.class.getName() ;
	
	//命令处理(通信服务器处理命令，不代表发送命令给测控器成功与否)是否成功，
	private Boolean success ;
	//命令处理情况的消息
	private String message ;
	//命令所针对的测控器当前在线情况
	private Boolean meterOnline ;
	
	public String toString(){
		String s = "" ;
		s += "success" + ":" + this.success.toString() + "\\" ;
		s += "message" + ":" + this.message + "\\" ;
		s += "meterOnline" + ":" + this.meterOnline.toString()  ;
		
		return s ;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getMeterOnline() {
		return meterOnline;
	}
	public void setMeterOnline(Boolean meterOnline) {
		this.meterOnline = meterOnline;
	}

}
