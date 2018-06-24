package com.am.cs12.config.conf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.am.cs12.config.RTUProtocolVO ;
import com.am.cs12.commu.protocol.*;
import com.am.util.NumberUtil;
import com.am.cs12.util.*;


public class ProtocolRTUConfig extends Config {

	private HashMap<String, RTUProtocolVO> RTUList ;
	
	public ProtocolRTUConfig(){
		this.RTUList = new HashMap<String, RTUProtocolVO>() ;
	}

	
	/**
	 * 分析RTU协议配置
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, RTUProtocolVO> parseOptions(String filePath) throws IllegalArgumentException {
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
		
		Element RTUs = root.getChild(ProtocolRTUConstant.RTUs_key);
		if (RTUs == null) {
			throw new IllegalArgumentException("出错！" + filePath + "中元素" + ProtocolRTUConstant.RTUs_key + "不存在！");
		}
		String name = null;
		String value = null;
		int tempInt = 0 ;
		List rtuList = RTUs.getChildren();
		Iterator rtuIt = rtuList.iterator();
		Element rtu = null;
		List rtuAtList = null ;
		Iterator rtuAtIt = null ;
		Element rtuAt = null;
		while (rtuIt.hasNext()) {
			rtu = (Element)rtuIt.next();
			rtuAtList = rtu.getChildren();
			rtuAtIt = rtuAtList.iterator();
			RTUProtocolVO vo = new RTUProtocolVO();
			while (rtuAtIt.hasNext()) {
				rtuAt = (Element) rtuAtIt.next();
				name = rtuAt.getName();
				value = rtuAt.getValue();
				if(value != null){
					value = value.trim() ;
				}else{
					throw new IllegalArgumentException(filePath + "中元素" +name + "数值为空！");
				}

				if (name.equals(ProtocolRTUConstant.name_key)) {
					vo.name = value ;
				}else if (name.equals(ProtocolRTUConstant.driverName_key)) {
					vo.driverName = value ;
				}else if (name.equals(ProtocolRTUConstant.onLine_key)) {
					vo.onLine = value ;
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.onLine_RTUIdType_key) ;
					if (!value.equals(ProtocolRTUConstant.ASCII)
									&& !value.equals(ProtocolRTUConstant.INT)  
									&& !value.equals(ProtocolRTUConstant.BCD)
									&& !value.equals(ProtocolRTUConstant.P206)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.onLine_RTUIdType_key + "配置值必须是ASCII或int或BCD或者206！");
					}
					vo.idType = value ;
					
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.onLine_from_key) ;
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.onLine_from_key + "配置数值必须是整数！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt <= -1) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.onLine_from_key + "配置值必须是0或正整数！");
					}
					vo.idFrom = tempInt ;
					
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.onLine_end_key) ;
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.onLine_end_key + "配置数值必须是整数！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt <= vo.idFrom ) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.onLine_end_key + "配置值必须是大于1的正整数！");
					}
					vo.idEnd = tempInt ;
					
				}else if (name.equals(ProtocolRTUConstant.complete_key)) {
					vo.complete = value ;
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.complete_lenType_key) ;
					if (!value.equals(ProtocolRTUConstant.ASCII)
									&& !value.equals(ProtocolRTUConstant.INT)  
									&& !value.equals(ProtocolRTUConstant.BCD)
									&& !value.equals(ProtocolRTUConstant.P206)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.complete_lenType_key + "配置值必须是ASCII或int或者BCD或者206！");
					}
					vo.lenType = value ;
					
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.complete_from_key) ;
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.complete_from_key + "配置数值必须是整数！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt <= -1) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.complete_from_key + "配置值必须是0或正整数！");
					}
					vo.lenFrom = tempInt ;
					
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.complete_end_key) ;
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.complete_end_key + "配置数值必须是整数！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt < vo.lenFrom ) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.complete_end_key + "配置值必须是大于lenFrom的正整数！");
					}
					vo.lenEnd = tempInt ;
					
					value = rtuAt.getAttributeValue(ProtocolRTUConstant.complete_extraLen_key) ;
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.complete_extraLen_key + "配置数值必须是整数！");
					}
					tempInt = Integer.parseInt(value) ;
					vo.extraLen = tempInt ;
					
				}else if (name.equals(ProtocolRTUConstant.sendHeartBeat_key)) {
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.sendHeartBeat_key + "配置数值必须是1或0或者-1的数字！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt < -1 || tempInt > 1) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.sendHeartBeat_key + "配置数值必须是1或0或者-1的数字！");
					}
					vo.sendHeartBeat = tempInt ;
				}else if (name.equals(ProtocolRTUConstant.heartBeatString_key )) {
					vo.heartBeatString = value ;
				}else if (name.equals(ProtocolRTUConstant.heartBeatInterval_key)) {
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.heartBeatInterval_key + "配置数值必须是大于或等于10(秒)整数！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt < 10) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.heartBeatInterval_key + "配置数值必须是大于或等于10(秒)整数！");
					}
					vo.heartBeatInterval = tempInt ;
				}else if (name.equals(ProtocolRTUConstant.returnHeartBeat_key)) {
					if (!NumberUtil.isIntNumber(value)) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.returnHeartBeat_key + "配置数值必须是1或0或者-1的数字！");
					}
					tempInt = Integer.parseInt(value) ;
					if (tempInt < -1 || tempInt > 1) {
						throw new IllegalArgumentException(filePath
								+ "中元素" + ProtocolRTUConstant.returnHeartBeat_key + "配置数值必须是1或0或者-1的数字！");
					}
					vo.returnHeartBeat = tempInt ;
				}
				
			}
			this.check(filePath, vo, RTUList);
			this.initDriver(filePath, vo) ;
			RTUList.put(vo.name, vo) ;
		}
		return RTUList ;
	}

	/**
	 * 检查RTU协议配置
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	private void check(String filePath, RTUProtocolVO vo,
			HashMap<String, RTUProtocolVO> RTUList) throws IllegalArgumentException {
		if (RTUList.containsKey(vo.name)) {
			throw new IllegalArgumentException(filePath + "中RTU协议配置中，协议名称有重名现象！");
		}
	}
	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private void initDriver(String filePath, RTUProtocolVO vo) throws IllegalArgumentException {
		String clazz = vo.driverName;
		try {
			Class c = Class.forName(clazz);
			if (c == null) {
				throw new IllegalArgumentException(filePath + "中RTU协议(" + vo.name + ")驱动类" + clazz + "不存在！");
			}
			@SuppressWarnings("unused")
			Object o = (DriverMeter) c.getDeclaredConstructor(String.class).newInstance(vo.name);
		} catch (Exception e) {
			throw new IllegalArgumentException(filePath + "中RTU协议(" + vo.name + ")驱动类" + clazz + "不存在！");
		}
	}
}
