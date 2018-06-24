package com.am.cs12.commu.protocol.amLocal;

import java.util.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class OnLine {
	public static String ALIAS = OnLine.class.getName() ;

	private HashMap<String , Boolean> onLineMap ;
	
	public OnLine(){
		this.onLineMap = new HashMap<String , Boolean>() ;
	}
	/**
	 * 对象转成xml
	 * @return
	 * @throws Exception
	 */
	public String toXml()throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(OnLine.ALIAS , OnLine.class);
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
	public static OnLine toObject(byte[] bxml)throws Exception{
		if(bxml == null || bxml.length == 0){
			throw new Exception("出错！不能将空字节数组转成OnLine对象！") ;
		}
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("onLine", OnLine.class);
			OnLine obj = (OnLine) xstream.fromXML(new String(bxml));
			return obj ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e ) ;
		}
	}
	
	/**
	 * 存入测控器在线情况
	 * @param rtuId
	 * @param onLine
	 */
	public void setOnLine(String rtuId , boolean onLine){
		this.onLineMap.put(rtuId, new Boolean(onLine)) ;
	}
	
	public HashMap<String, Boolean> getOnLineMap() {
		return onLineMap;
	}



	
}
