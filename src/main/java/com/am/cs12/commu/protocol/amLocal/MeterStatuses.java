package com.am.cs12.commu.protocol.amLocal;

import java.util.HashMap;

import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MeterStatuses {
	public static String ALIAS = MeterStatuses.class.getName() ;

	private HashMap<String , MeterStatus> meterStatusMap ;
	
	/**
	 * 对象转成xml
	 * @return
	 * @throws Exception
	 */
	public String toXml()throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(MeterStatuses.ALIAS , MeterStatuses.class);
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
	public static MeterStatuses toObject(byte[] bxml)throws Exception{
		if(bxml == null || bxml.length == 0){
			throw new Exception("出错！不能将空字节数组转成MeterStatuses对象！") ;
		}
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("meterStatus", MeterStatuses.class);
			MeterStatuses obj = (MeterStatuses) xstream.fromXML(new String(bxml));
			return obj ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e ) ;
		}
	}
	public HashMap<String, MeterStatus> getMeterStatusMap() {
		return meterStatusMap;
	}
	public void setMeterStatusMap(HashMap<String, MeterStatus> meterStatusMap) {
		this.meterStatusMap = meterStatusMap;
	}
	


}
