package com.am.cs12.commu.protocol;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Data {
	
	public static String ALIAS = Data.class.getName() ;

	private String id ;//测控终端ID
	private String commandId ;//如果是命令回数据，则其对应命令ID
	
	protected String code ;//测控终端数据所对应的功能码
	protected String code_indentifer;//标识符引导符
	private String clock ;//测控终端时钟

	protected Object subData ;//具体协议的数据

	private String hex ;//上报数据的十六进制
	
	private int serialNum;//流水号
	private int stationType;//测站分类码
	
	
	/**
	 * 对象转成xml
	 * @return
	 * @throws ACException
	 */
	public String toXml()throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(Data.ALIAS , Data.class);
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
	public Data toObject(String xml)throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(Data.ALIAS, Data.class);
			Data obj = (Data) xstream.fromXML(xml);
			return obj ;
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
	public Data toObject(byte[] bxml)throws Exception{
		if(bxml == null || bxml.length == 0){
			throw new Exception("出错！不能将空字节数组转成Data对象！") ;
		}
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("Data", Data.class);
			Data obj = (Data) xstream.fromXML(new String(bxml));
			return obj ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e ) ;
		}
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

	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}

	public Object getSubData() {
		return subData;
	}

	public void setSubData(Object subData) {
		this.subData = subData;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getHex() {
		return hex;
	}

	public void setHex(String hex) {
		this.hex = hex;
	}
	public int getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}
	public int getStationType() {
		return stationType;
	}
	public void setStationType(int stationType) {
		this.stationType = stationType;
	}
	public String getCode_indentifer() {
		return code_indentifer;
	}
	public void setCode_indentifer(String code_indentifer) {
		this.code_indentifer = code_indentifer;
	}

}
