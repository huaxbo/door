package com.am.cs12.commu.protocolSate;

public class DataSate {

	protected String code ;//卫星终端(卫星基站)命令的功能码
	
	protected String Tlen;//数据总长度
	
	protected String addr;//用户地址
	
	protected String type;//信息类别
	
	protected String idSate ;//发数据方--卫星终端(卫星基站)ID，或叫用户ID
	
	protected String hour;//时
	
	protected String minite;//分
	
	protected String len;//电文长度
	
	protected Object subData ;//数据
	
	private long timeStamp;//时间戳
	
	
	
	public String getTlen() {
		return Tlen;
	}

	public void setTlen(String tlen) {
		Tlen = tlen;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinite() {
		return minite;
	}

	public void setMinite(String minite) {
		this.minite = minite;
	}
	

	public DataSate(){
		this.timeStamp = System.currentTimeMillis() ;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getSubData() {
		return subData;
	}

	public void setSubData(Object subData) {
		this.subData = subData;
	}

	public String getIdSate() {
		return idSate;
	}

	public void setIdSate(String idSate) {
		this.idSate = idSate;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
	
	public String getLen() {
		return len;
	}

	public void setLen(String len) {
		this.len = len;
	}
	
	
}
