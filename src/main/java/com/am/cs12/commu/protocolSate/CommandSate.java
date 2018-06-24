package com.am.cs12.commu.protocolSate;

import java.util.HashMap;

public class CommandSate {

	private String code ;
	
	/**
	 * 参数java Bean 集合
	 */
	private HashMap<String , Object>  params ;

	/**
	 * 构造方法，初始化参数据集合(HashMap对象)
	 */
	public CommandSate(){
		params = new HashMap<String , Object>() ;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}


	
}
