package com.am.cs12.commu.protocolSate.tianHong.$TINF;

public class Data_TINF {

	private String hour ;
	private String minute ;
	private String second ;
	
	
	@Override
	public String toString() {
		return "卫星授时：[hour=" + hour + ", minute=" + minute + ", second="
				+ second + "]";
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
}
