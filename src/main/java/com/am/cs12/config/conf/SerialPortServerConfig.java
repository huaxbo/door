package com.am.cs12.config.conf;

import java.util.*;

import org.jdom.Element;

import com.am.cs12.config.*;
import com.am.cs12.util.*;
import com.am.util.*;

public class SerialPortServerConfig  extends Config {

	private HashMap<String, SerialPortVO> portsMap ;
	
	public SerialPortServerConfig(){
		this.portsMap = new HashMap<String, SerialPortVO>() ;
	}

	
	/**
	 * 分析串口配置
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, SerialPortVO> parseOptions(String filePath) throws IllegalArgumentException {
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
		Element ports = root.getChild(SerialPortServerConstant.ports_key);
		if (ports == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到" + SerialPortServerConstant.ports_key + "元素!");
		}
		
		List portList = ports.getChildren(SerialPortServerConstant.port_key);
		if (portList == null) {
			return this.portsMap ;
		}
		Iterator portIt = portList.iterator();
		Element port = null;
		
		String attValue = null ;
		while (portIt.hasNext()) {
			port = (Element)portIt.next();
			
			attValue = port.getAttributeValue(SerialPortServerConstant.enable_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.enable_key + "属性未配置！");
			}else{
				if(!attValue.equals("true") && !attValue.equals("false")){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.enable_key + "属性值必须为true或false！");
				}
				if(attValue.equals("false")){
					continue ;
				}
			}

			SerialPortVO portVo = new SerialPortVO() ;

			attValue = port.getAttributeValue(SerialPortServerConstant.debugForNoTerminal_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.debugForNoTerminal_key + "属性未配置！");
			}else{
				if(!attValue.equals("true") && !attValue.equals("false")){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.debugForNoTerminal_key + "属性值必须为true或false！");
				}
				portVo.setDebugForNoTerminal(Boolean.parseBoolean(attValue)) ;
			}


			attValue = port.getAttributeValue(SerialPortServerConstant.id_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.id_key + "属性未配置！");
			}else{
				if(this.isExistPortId(attValue , this.portsMap)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.id_key + "属性值重复配置！");
				}
				portVo.setId(attValue) ;
			}
			
			attValue = port.getAttributeValue(SerialPortServerConstant.name_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.name_key + "属性未配置！");
			}else{
				if(attValue == null || !attValue.startsWith("COM")){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.name_key + "必须为COM* ！");
				}
				portVo.setName(attValue) ;
			}
			
			
			attValue = port.getAttributeValue(SerialPortServerConstant.baudrate_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.baudrate_key + "属性未配置！");
			}else{
				if(!NumberUtil.isIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.baudrate_key  + "必须为整数！");
				}
				if(Integer.parseInt(attValue) <= 0){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.baudrate_key + "必须大于等于0！");
				}
				portVo.setBaudrate(attValue) ;
			}
			
			attValue = port.getAttributeValue(SerialPortServerConstant.dataBits_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.dataBits_key + "属性未配置！");
			}else{
				if(!NumberUtil.isIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.dataBits_key  + "必须为整数！");
				}
				if(Integer.parseInt(attValue) < 0){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.dataBits_key + "必须大于等于0！");
				}
				portVo.setDataBits(attValue) ;
			}
			
			
			attValue = port.getAttributeValue(SerialPortServerConstant.stopBits_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.stopBits_key + "属性未配置！");
			}else{
				if(!NumberUtil.isIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.stopBits_key  + "必须为整数！");
				}
				if(Integer.parseInt(attValue) < 0){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.stopBits_key + "必须大于等于0！");
				}
				portVo.setStopBits(attValue) ;
			}

			//none：不校验；odd：奇校验；even：偶校验
			attValue = port.getAttributeValue(SerialPortServerConstant.parity_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.parity_key + "属性未配置！");
			}else{
				if(!attValue.equals("none") && !attValue.equals("odd") && !attValue.equals("even")){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.parity_key + "属性值必须为none、odd或even！");
				}
				portVo.setParity(attValue) ;
			}
			
			
			attValue = port.getAttributeValue(SerialPortServerConstant.waitTime_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.waitTime_key + "属性未配置！");
			}else{
				if(!NumberUtil.isIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.waitTime_key  + "必须为整数！");
				}
				if(Integer.parseInt(attValue) < 0){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.waitTime_key + "必须大于等于0！");
				}
				portVo.setWaitTime(Integer.parseInt(attValue)) ;
			}

			attValue = port.getAttributeValue(SerialPortServerConstant.protocolType_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.protocolType_key + "属性未配置！");
			}else{
				if(!attValue.equals(AmConstant.protocolType_satellite)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.protocolType_key + "属性值必须为" + AmConstant.protocolType_satellite + "！");
				}
				portVo.setProtocolType(attValue) ;
			}
			
			
			
			attValue = port.getAttributeValue(SerialPortServerConstant.protocolName_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.protocolName_key + "属性未配置！");
			}else{
				if(!this.isExistProtocol(attValue, portVo.getProtocolType())){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.protocolName_key + "协议名称在protocol.xml配置中不存在！");
				}
				portVo.setProtocolName(attValue) ;
			}
			
			
			attValue = port.getAttributeValue(SerialPortServerConstant.sendInterval_key) ;
			if(attValue != null){
				attValue = attValue.trim() ;
			}
			if(attValue == null || attValue.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.sendInterval_key + "属性未配置！");
			}else{
				if(!NumberUtil.isIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.sendInterval_key  + "必须为整数！");
				}
				if(Integer.parseInt(attValue) < 100){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.sendInterval_key + "必须大于等于100(毫秒)！");
				}
				if(Integer.parseInt(attValue) > 600000){
					throw new IllegalArgumentException(filePath + "配置有错误，port元素的" + SerialPortServerConstant.sendInterval_key + "必须小于等于600000(毫秒)！");
				}
				portVo.setSendInterval(Integer.parseInt(attValue)) ;
			}
			
			portsMap.put(portVo.getId() , portVo) ;
		}
		
		return portsMap ;
	}

	/**
	 * 检查串口的ID是否重复
	 * @param id
	 * @param portsMap
	 */
	private boolean isExistPortId(String id , HashMap<String, SerialPortVO> portsMap) {
		if (portsMap.containsKey(id)) {
			return true ;
		}
		return false ;
	}
	
	/**
	 * 是否存在串口对应的协议
	 * @param p
	 * @return
	 */
	private boolean isExistProtocol(String p , String pType){
		if(pType.equals(AmConstant.protocolType_satellite)){
			HashMap<String, SateProtocolVO> map = ConfigCenter.instance().getSateProtocolMap() ;
			if(map != null){
				if(map.containsKey(p)){
					return true ;
				}
			}
		}
		return false ;
	}

}
