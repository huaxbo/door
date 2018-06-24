package com.am.cs12.commu.core.innerCommand;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.command.*;
import com.am.cs12.commu.protocol.amLocal.* ;
import com.am.cs12.commu.protocol.ProtocolParam;
import com.am.cs12.commu.core.innerCommand.help.*;
import com.am.cs12.commu.protocol.amLocal.MeterStatuses ;

public class InnerCommandDealer {
	private static Logger log = LogManager.getLogger(InnerCommandDealer.class.getName()) ;
	/**
	 * 处理内部命令
	 * @param localSession
	 * @param com
	 * @throws Exception 
	 */
	public void deal(IoSession localSession, Command com) throws Exception{
		HashMap<String, Object> params = com.getParams() ;
		if(params == null || params.size() == 0){
			errored(localSession , "出错，收到业务系统传来的命令参数集合为空！") ;
			return ;
		}
		ProtocolParam pp = (ProtocolParam)params.get(ProtocolParam.KEY) ;
		String code = pp.getCode() ;
		if(code == null || code.equals("")){
			errored(localSession , "出错，收到业务系统传来的命令参数集合中无功能码！") ;
			return ;
		}
		Param p = (Param)params.get(Param.KEY) ; ;
//		if(code.equals(Code.onLine_some)){
//			p = (Param)params.get(Param.KEY) ;
//			if(p == null){
//				errored(localSession , "出错，查询部分测控器运行数据，必须提供参数据Bean:" + Param.KEY) ;
//				return ;
//			}
//		}
		
		if(code.equals(Code.onLine)){
			//查询在线与不在线情况
			this.onLine(localSession , com) ;
		}else if(code.equals(Code.onLine_yes)){
			this.onLineYes(localSession , com) ;
		}else if(code.equals(Code.onLine_no)){
			this.onLineNo(localSession , com) ;
		}else if(code.equals(Code.onLine_some)){
			this.onLineSome(localSession , com , p) ;
		}else if(code.equals(Code.allStatus)){
			this.allMeterStatus(localSession, com) ;
		}else if(code.equals(Code.someStatus)){
			this.someMeterStatus(localSession, com, p) ;
		}else if(code.equals(Code.config)){
			log.info("当前未实现") ;
		}else if(code.equals(Code.clock)){
			this.clock(localSession , com) ;
		}
	}
	
	/**
	 * 查询在线情况
	 * @param localSession
	 * @throws Exception 
	 */
	private void onLine(IoSession localSession , Command com) throws Exception{
		OnLine ol = new HelpOnLine().onLine() ;
		this.successed(localSession, "查询在线情况结果。", com.getId(), ol.toXml().getBytes()) ;
	}
	
	
	/**
	 * 查询在线情况
	 * @param localSession
	 * @throws Exception 
	 */
	private void onLineYes(IoSession localSession , Command com) throws Exception{
		OnLine ol = new HelpOnLine().onLineYes() ;
		this.successed(localSession, "查询在线结果。", com.getId(), ol.toXml().getBytes()) ;
	}
	
	
	/**
	 * 查询在线情况
	 * @param localSession
	 * @throws Exception 
	 */
	private void onLineNo(IoSession localSession , Command com) throws Exception{
		OnLine ol = new HelpOnLine().onLineNo() ;
		this.successed(localSession, "查询不在线结果。", com.getId(), ol.toXml().getBytes()) ;
	}
	
	
	/**
	 * 查询在线情况
	 * @param localSession
	 * @throws Exception 
	 */
	private void onLineSome(IoSession localSession , Command com, Param p) throws Exception{
		String[] ids = p.getIds() ;
		OnLine ol = new HelpOnLine().onLineSome(ids) ;
		this.successed(localSession, "查询一些在线情况结果。", com.getId(), ol.toXml().getBytes()) ;
	}
	
	/**
	 * 查询通信服务器时钟
	 * @param localSession
	 * @throws Exception 
	 */
	private void clock(IoSession localSession , Command com) throws Exception{
		Clock c = new HelpClock().clock() ;
		this.successed(localSession, "查询通信服务器时钟。", com.getId(), c.toXml().getBytes()) ;
	}
	
	/**
	 * 查询测控终端状态
	 * @param localSession
	 * @throws Exception 
	 */
	private void allMeterStatus(IoSession localSession , Command com) throws Exception{
		MeterStatuses ms = new HelpMeterStatus().allMeterStatuses() ;
		this.successed(localSession, "查询测控终端状态。", com.getId(), ms.toXml().getBytes()) ;
	}
	
	/**
	 * 查询测控终端状态
	 * @param localSession
	 * @throws Exception 
	 */
	private void someMeterStatus(IoSession localSession , Command com, Param p) throws Exception{
		String[] ids = p.getIds() ;
		MeterStatuses ms = new HelpMeterStatus().someMeterStatuses(ids) ;
		this.successed(localSession, "查询测控终端状态。", com.getId(), ms.toXml().getBytes()) ;
	}
	
	/**
	 * 处理命令成功
	 * @param message
	 */
	private void successed(IoSession localSession, String message, String commandId, byte[] attachment ){
		if(localSession != null){
			Command com = new Command() ;
			com.createReturnSuccessCommand(message, commandId) ;
			MinaData minaData = new MinaData() ;
			minaData.setCom(com) ;
			minaData.setAttachment(attachment) ;
			this.returnLocal(localSession, minaData) ;
		}
	}
	/**
	 * 处理命令发生错误
	 * @param message
	 */
	private void errored(IoSession localSession, String message){
		log.error(message) ;
		if(localSession != null){
			this.returnLocal(localSession, new Command().createReturnErrorCommand(message, Command.defaultId)) ;
		}
	}
	
	/**
	 * 向业务系统回馈
	 * @param localSession
	 * @param reCom
	 */
	private void returnLocal(IoSession localSession, Command reCom){
		if(localSession != null){
			localSession.write(reCom) ;
		}
	}
	/**
	 * 向业务系统回馈
	 * @param localSession
	 * @param reCom
	 */
	private void returnLocal(IoSession localSession, MinaData minaData){
		if(localSession != null){
			localSession.write(minaData) ;
		}
	}


}
