package com.am.cs12.config.conf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import com.am.util.NumberUtil;
import com.am.cs12.util.*;
import com.am.cs12.config.SateProtocolVO;
import com.am.cs12.commu.protocolSate.DriverSate;

public class ProtocolSateConfig extends Config {

	private HashMap<String, SateProtocolVO> SateList ;
	
	public ProtocolSateConfig(){
		this.SateList = new HashMap<String, SateProtocolVO>() ;
	}

	
	/**
	 * 分析卫星协议配置
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, SateProtocolVO> parseOptions(String filePath) throws IllegalArgumentException {
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
		
		Element Sates = root.getChild(ProtocolSateConstant.sates_key);
		if (Sates == null) {
			throw new IllegalArgumentException("出错！" + filePath + "中元素" + ProtocolSateConstant.sates_key + "不存在！");
		}
		String name = null;
		String value = null;
		List rtuList = Sates.getChildren();
		Iterator rtuIt = rtuList.iterator();
		Element rtu = null;
		List rtuAtList = null ;
		Iterator rtuAtIt = null ;
		Element rtuAt = null;
		while (rtuIt.hasNext()) {
			rtu = (Element)rtuIt.next();
			rtuAtList = rtu.getChildren();
			rtuAtIt = rtuAtList.iterator();
			SateProtocolVO vo = new SateProtocolVO();
			while (rtuAtIt.hasNext()) {
				rtuAt = (Element) rtuAtIt.next();
				name = rtuAt.getName();
				value = rtuAt.getValue();
				if(value != null){
					value = value.trim() ;
				}else{
					throw new IllegalArgumentException(filePath + "中元素" +name + "数值为空！");
				}

				if (name.equals(ProtocolSateConstant.name_key)) {
					vo.name = value ;
				}else if (name.equals(ProtocolSateConstant.driverName_key)) {
					vo.driverName = value ;
				}else if (name.equals(ProtocolSateConstant.enableGrantTime_key)) {
					if(value.equalsIgnoreCase("true")){
						vo.enableGrantTime = true ;
					}else{
						vo.enableGrantTime = false ;
					}
				}else if (name.equals(ProtocolSateConstant.grantTimeStartHour_key)) {
					if(!NumberUtil.isPlusIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" +name + "数值必须为数字！");
					}
					vo.grantTimeStartHour = Integer.parseInt(value) ;
					if(vo.grantTimeStartHour < 0 || vo.grantTimeStartHour > 23){
						throw new IllegalArgumentException(filePath + "中元素" +name + "数值必须大于等于0或小于等于23！");
					}
				}else if (name.equals(ProtocolSateConstant.grantTimeStartMinute_key)) {
					if(!NumberUtil.isPlusIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" +name + "数值必须为数字！");
					}
					vo.grantTimeStartMinute = Integer.parseInt(value) ;
					if(vo.grantTimeStartMinute < 10 || vo.grantTimeStartMinute > 50){
						throw new IllegalArgumentException(filePath + "中元素" +name + "数值必须大于等于10或小于等于50！");
					}
				}else if (name.equals(ProtocolSateConstant.grantTimeByDeviate_key)) {
					if(!NumberUtil.isPlusIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" +name + "数值必须为数字！");
					}
					vo.grantTimeByDeviate = Integer.parseInt(value) ;
					if(vo.grantTimeByDeviate < 1 || vo.grantTimeByDeviate > 5){
						throw new IllegalArgumentException(filePath + "中元素" +name + "数值必须大于等于1或小于等于5！");
					}
				}

			}
			this.check(filePath, vo, SateList);
			this.initDriver(filePath, vo) ;
			SateList.put(vo.name, vo) ;
		}
		
		return SateList ;
	}

	/**
	 * 检查卫星协议配置
	 * @param filePath
	 * @param vo
	 * @param Sates
	 * @throws IllegalArgumentException
	 */
	private void check(String filePath, SateProtocolVO vo,
			HashMap<String, SateProtocolVO> SateList) throws IllegalArgumentException {
		if (SateList.containsKey(vo.name)) {
			throw new IllegalArgumentException(filePath + "中卫星协议配置中，协议名称有重名现象！");
		}
	}
	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param Sates
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private void initDriver(String filePath, SateProtocolVO vo) throws IllegalArgumentException {
		String clazz = vo.driverName ;
		try{
			Class c = Class.forName(clazz);
			if (c == null) {
				throw new IllegalArgumentException(filePath + "中卫星协议(" + vo.name + ")驱动类" + clazz + "不存在！");
			}
			DriverSate o = (DriverSate)c.getDeclaredConstructor(String.class).newInstance(vo.name);
			if(vo.enableGrantTime){
				//用线程等待其他部分都初始化完成
				new StartGrantTimeTaskThread(o).start() ;
			}
		}catch(Exception e){
			throw new IllegalArgumentException(filePath + "中卫星协议(" + vo.name + ")驱动类" + clazz + "不存在！");
		}
	}
	
	private class StartGrantTimeTaskThread extends Thread{
		private DriverSate driver ;
		public StartGrantTimeTaskThread(DriverSate driver){
			this.driver = driver ;
		}
		public void run(){
			boolean hasEx = false ;
				try {
					sleep(5000l) ;
				} catch (InterruptedException e) {
					e.printStackTrace();
					hasEx = true ;
				}finally{
					if(hasEx){
						try {
							sleep(5000l) ;
						} catch (InterruptedException e) {
							e.printStackTrace();
							hasEx = true ;
						}finally{
							this.driver.startGrantTime() ;
						}
					}else{
						this.driver.startGrantTime() ;
					}
				}
		}
	}
}
