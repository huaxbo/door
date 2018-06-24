package com.am.cs12.commu.protocol;

public class ProtocolParam {

	public static final String KEY = ProtocolParam.class.getName() ;
	
	/**
	 * 测控器 编码 
	 */
	private String id ;
	
	/**
	 * 命令的功能码
	 */
	private String code ;
	/**
	 * 被透传命令的功能码
	 */
	private String subCode  ;
	
	public String toString(){
		String s = "" ;
		s += "idd" + ":" + this.id + "\\" ;
		s += "code" + ":" + code + "\\" ;
		s += "subCode" + ":" + subCode ;
		
		return s ;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
}
