package com.am.cs12.commu.protocol.amLocal;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Clock {

	public static String ALIAS = Clock.class.getName() ;

	private String clock ;
	private Integer year ;
	private Integer month ;
	private Integer date ;
	private Integer hour ;
	private Integer minute ;
	private Integer second ;
	/**
	 * 对象转成xml
	 * @return
	 * @throws Exception
	 */
	public String toXml()throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(Clock.ALIAS , Clock.class);
			return xstream.toXML(this);
		}catch(Exception e){
			throw new Exception(e.getMessage() , e ) ;
		}
	}
	/**
	 * xml转成对象
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Clock toObject(byte[] bxml)throws Exception{
		if(bxml == null || bxml.length == 0){
			throw new Exception("出错！不能将空字节数组转成Clock对象！") ;
		}
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("clock", Clock.class);
			Clock obj = (Clock) xstream.fromXML(new String(bxml));
			return obj ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e ) ;
		}
	}
	
	public String getClock() {
		return clock;
	}
	public void setClock(String clock) {
		this.clock = clock;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getDate() {
		return date;
	}
	public void setDate(Integer date) {
		this.date = date;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	public Integer getSecond() {
		return second;
	}
	public void setSecond(Integer second) {
		this.second = second;
	}
	
}
