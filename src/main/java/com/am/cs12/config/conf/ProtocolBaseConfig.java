package com.am.cs12.config.conf ;

import java.util.Iterator;
import java.util.List;
import java.lang.IllegalArgumentException ;
import org.jdom.Element;

import com.am.util.*;
import com.am.cs12.config.*;
import com.am.cs12.util.*;

public class ProtocolBaseConfig extends Config{
	
	private ProtocolBaseVO vo  ;
	
	public ProtocolBaseConfig(){
		this.vo = ProtocolBaseVO.instance() ;
	}

	
	/**
	 * 分析协议基础配置
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public ProtocolBaseVO parseOptions(String filePath) throws IllegalArgumentException {
		if(this.doc == null){
			super.createDom(filePath) ;
		}
		if(this.doc == null){
			throw new IllegalArgumentException(filePath + "语法有错误，不能创建xml文件的doc对象!");
		}
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}
		
		
		List list = root.getChildren();
		Iterator it = list.iterator();
		Element e = null;
		String name = null ;
		String value = null ;
		int tempInt = 0 ;
		while (it.hasNext()) {
			e = (Element) it.next();
			name = e.getName() ;
			value = e.getText() ;
			if(value != null){
				value = value.trim() ;
			}else{
				throw new IllegalArgumentException(filePath + "中元素" +name + "数值为空！");
			}
			
			if (name.equals(ProtocolBaseConstant.headMinLength_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.headMinLength_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 1 || tempInt > 20){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.headMinLength_key + "配置数值必须在1(包括)至20(包括)间取值！");
				}
				vo.headMinLength = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.maxSendCommandTimes_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.maxSendCommandTimes_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 1 || tempInt > 10){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.maxSendCommandTimes_key + "配置数值必须在1(包括)至10(包括)间取值！");
				}
				vo.maxSendCommandTimes = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.sendCommandInterval_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.sendCommandInterval_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 10 || tempInt > 600){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.sendCommandInterval_key + "配置数值必须在10秒(包括)至600秒(包括)间取值！");
				}
				vo.sendCommandInterval = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.commandTimeout_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.commandTimeout_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				vo.commandTimeout = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.confirmCommandInterval_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.confirmCommandInterval_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 1 || tempInt > 5){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.confirmCommandInterval_key + "配置数值必须是1至5的整数！");
				}
				vo.confirmCommandInterval = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.maxContinueFailCommandTimes_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.maxContinueFailCommandTimes_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 0){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.maxContinueFailCommandTimes_key + "配置数值必须是大于等于0的整数！");
				}
				vo.maxContinueFailCommandTimes = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.minMeterConnectIdleInterval_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.minMeterConnectIdleInterval_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 1 || tempInt > 65){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.minMeterConnectIdleInterval_key + "配置数值必须是大于等于1小于等于65的整数！");
				}
				vo.minMeterConnectIdleInterval = tempInt ;
			}else if(name.trim().equals(ProtocolBaseConstant.checkConnectOffCloseSocketForIdleInterval_key)){
				if(value == null || (!value.equals("true") && !value.equals("false")) ){
					throw new IllegalArgumentException(filePath + "中元素"+ ProtocolBaseConstant.checkConnectOffCloseSocketForIdleInterval_key + "配置必须是true或false！");
				}
				vo.checkConnectOffCloseSocketForIdleInterval = Boolean.parseBoolean(value) ;
			}else if(name.trim().equals(ProtocolBaseConstant.checkConnectOffCloseSocketForContinueFailCommandTimes_key)){
				if(value == null || (!value.equals("true") && !value.equals("false")) ){
					throw new IllegalArgumentException(filePath + "中元素"+ ProtocolBaseConstant.checkConnectOffCloseSocketForContinueFailCommandTimes_key + "配置必须是true或false！");
				}
				vo.checkConnectOffCloseSocketForContinueFailCommandTimes = Boolean.parseBoolean(value) ;
			}else if(name.trim().equals(ProtocolBaseConstant.synchronizeClockEnable_key)){
				if(value == null || (!value.equals("true") && !value.equals("false")) ){
					throw new IllegalArgumentException(filePath + "中元素"+ ProtocolBaseConstant.synchronizeClockEnable_key + "配置必须是true或false！");
				}
				vo.synchronizeClockEnable = Boolean.parseBoolean(value) ;
			}else if (name.equals(ProtocolBaseConstant.synchronizeClockDifference_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.synchronizeClockDifference_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 5 || tempInt > 30){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.synchronizeClockDifference_key + "配置数值必须是大于等于5小于等于30的整数！");
				}
				vo.synchronizeClockDifference = tempInt ;
			}else if (name.equals(ProtocolBaseConstant.synchronizeClockdeny_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.synchronizeClockdeny_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 0 || tempInt > 24){
					throw new IllegalArgumentException(filePath
							+ "中元素" + ProtocolBaseConstant.synchronizeClockdeny_key + "配置数值必须是大于等于0小于等于23的整数！");
				}
				vo.synchronizeClockdeny = tempInt ;
			}
			
		}
		this.check(filePath, vo) ;
		
		return vo ;
	}
	
	private void check(String filePath , ProtocolBaseVO vo)throws IllegalArgumentException {
		//commandTimeout取值范围maxSendCommandTimes*sendCommandInterval 至 maxSendCommandTimes*sendCommandInterval*2
		if(vo.commandTimeout < vo.maxSendCommandTimes * vo.sendCommandInterval){
			throw new IllegalArgumentException(filePath
					+ "中元素" + ProtocolBaseConstant.commandTimeout_key + "配置数值必须是大于等于" 
					+ "元素maxSendCommandTimes与sendCommandInterval的乘积！" );
		}
		if(vo.commandTimeout > 10 * vo.maxSendCommandTimes * vo.sendCommandInterval){
			throw new IllegalArgumentException(filePath
					+ "中元素" + ProtocolBaseConstant.commandTimeout_key + "配置数值必须是小于等于" 
					+ "元素maxSendCommandTimes与sendCommandInterval的乘积的10倍！" );
		}
	}
}

