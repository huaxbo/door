package com.am.cs12.command ;

import java.util.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Command{
	public static String ALIAS = Command.class.getName() ;
	/**
	 * 默认命令ID
	 */
	public static final String defaultId = "999999999" ;
	
	/**
	 * 本条命令的ID
	 */
	private String id; 
	
	/**
	 * 命令类型:测控终端命令、针对通信服务器的命令
	 */
	private String type ;

	/**
	 * 参数java Bean 集合
	 */
	private HashMap<String , Object>  params ;


	/**
	 * 构造方法，初始化参数据集合(HashMap对象)
	 */
	public Command(){
		params = new HashMap<String , Object>() ;
	}

	/**
	 * 对象转成xml
	 * @return
	 * @throws Exception
	 */
	public String toXml()throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(Command.ALIAS , Command.class);
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
	public Command toObject(String xml)throws Exception{
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(Command.ALIAS, Command.class);
			Command obj = (Command) xstream.fromXML(xml);
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
	public Command toObject(byte[] bxml)throws Exception{
		if(bxml == null || bxml.length == 0){
			throw new Exception("出错！不能将空字节数组转成Command命令对象！") ;
		}
		try{
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("command", Command.class);
			Command obj = (Command) xstream.fromXML(new String(bxml));
			return obj ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e ) ;
		}
	}
	
	/**
	 * 创建返回的出错命令
	 * @param error
	 * @param commandId
	 * @return
	 */
	public Command createReturnErrorCommand(String error , String commandId){
		if(commandId == null){
			this.setId(defaultId) ;
		}else{
			this.setId(commandId) ;
		}
		
		FeedbackParam p = new FeedbackParam() ;
		p.setSuccess(false) ;
		p.setMessage(error) ;
		
		this.params.put(FeedbackParam.KEY, p) ;
		
		return this ;
	}
	/**
	 * 创建返回的简单成功命令，针对远程测控器命令
	 * @param error
	 * @param commandId
	 * @return
	 */
	public Command createReturnSuccessCommand(String message , String commandId, boolean remoteOnLine){
		if(commandId == null){
			this.setId(defaultId) ;
		}else{
			this.setId(commandId) ;
		}

		FeedbackParam p = new FeedbackParam() ;
		p.setSuccess(true) ;
		p.setMessage(message) ;
		p.setMeterOnline(remoteOnLine) ;
		
		this.params.put(FeedbackParam.KEY, p) ;
		
		return this ;
	}

	/**
	 * 创建返回的简单成功命令，针对通信服务器的命令
	 * @param error
	 * @param commandId
	 * @return
	 */
	public Command createReturnSuccessCommand(String message , String commandId){
		if(commandId == null){
			this.setId(defaultId) ;
		}else{
			this.setId(commandId) ;
		}

		FeedbackParam p = new FeedbackParam() ;
		p.setSuccess(true) ;
		p.setMessage(message) ;
		
		this.params.put(FeedbackParam.KEY, p) ;
		
		return this ;
	}

//	/**
//	 * 创建返回的简单成功命令
//	 * @param error
//	 * @param commandId
//	 * @return
//	 */
//	public Command createReturnSuccessSimpleCommand(String message , String commandId, boolean remoteOnLine){
//		if(commandId == null){
//			this.setId(defaultId) ;
//		}else{
//			this.setId(commandId) ;
//		}
//		HashMap<String , Object> reParams = this.getParams() ;
//		reParams.put(FeedbackKey.RESULT, FeedbackResult.success) ;
//		reParams.put(FeedbackKey.MESSAGE, message) ;
//		return this ;
//	}

	
	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
