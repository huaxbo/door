package com.am.cs12.config.meter ;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jdom.*;
import org.jdom.output.XMLOutputter;

import com.am.cs12.config.conf.*;
import com.am.cs12.util.AmConstant;
import com.am.util.ResUtil;


public class XMLOrperator {
	
	public static Object synObj = new Object()  ;
	
	/*
	 * 此类变量控制是否将meter.xml文件中测控终端的配置更新到内存中
	 * 如果通过下以下两个途径对meter.xml文件的更新将不更新到内存中
	 *   1、向测控终端下发更改RTU ID或RTU ID的命令，当命令应答收到，并显示测控终端成功执行命令，则
	 *      通信服务器会修改meter.xml文件配置信息，同时修改内存中的测控终端配置信息，此时updateMeterXmlFileToMemory设置为true，则
	 *      不需要重新解析meter.xml配置文件以更新内存中的测控终端配置信息；
	 *   2、由通信服务器web系统，增、删、改测控终端配置，其他同第一种情况；
	 * 如果直接修改meter.xml配置文件来实现  增、删、改测控终端配置，这时需要将重新解析meter.xml配置文件，并更新内存中的测控终端配置信息
	 */
	public static boolean updateMeterXmlFileToMemory = true ;
	
	/**
	 * 构造方法
	 *
	 */
	public XMLOrperator(){
	}
	
	/**
	 * 保存meters.xml文件更新时间到比较文件中
	 * 
	 */
	public void saveMetersXmlFileLastUpdateTime() throws Exception {
		long last = this.getMeterXMLFileLastUpdateTime() ;
		this.saveMetersXmlFileLastUpdateTime(last) ;
	}

	/**
	 * 从比较文件中得到保存的上次meters.xml的更新时间
	 * 
	 * @param compareFile
	 * @return
	 */
	public long getSavedMeterXMLFileLastUpdateTime()
			throws Exception {
		URL cofu = XMLOrperator.class.getResource(AmConstant.meterConfigFileTimeFilePath);
		if(cofu == null){
			try {
				cofu = new URL(ResUtil.getAppPath(XMLOrperator.class,AmConstant.meterConfigFileTimeFilePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		
		if (cofu == null) {
			throw new Exception("出错！保存meter.xml最后更新时间的文件" + AmConstant.meterConfigFileTimeFilePath + "不存在。");
		}
		File cof = new File(cofu.getFile());
		FileInputStream in = null;
		try {
			in = new FileInputStream(cof);
			byte[] bs = new byte[in.available()];
			in.read(bs);
			if (bs == null || bs.length == 0) {
				return 0;
			} else {
				return Long.parseLong(new String(bs));
			}
		} catch (Exception e) {
			throw new Exception("出错！将meter.xml最后更新时间写比较文件" + AmConstant.meterConfigFileTimeFilePath + "发生异常。");
		} finally {
			try {
				in.close();
			} catch (Exception ee) {
			} finally {
			}
		}
	}
	/**
	 * 得到meter.xml的实际最后更新时间
	 * 
	 * @param compareFile
	 * @return
	 */
	public long getMeterXMLFileLastUpdateTime()
			throws Exception {
		URL clfu = XMLOrperator.class.getResource(AmConstant.meterConfigFilePath);
		if(clfu == null){
			try {
				clfu = new URL(ResUtil.getAppPath(XMLOrperator.class,AmConstant.meterConfigFilePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		if (clfu == null) {
			throw new Exception("出错！配置文件meter.xml不存在。");
		}
		File clf = new File(clfu.getFile());
		long last = clf.lastModified();
		return last ;
	}
	/**
	 * 保存meter.xml文件更新时间可存在比较文件中
	 * 
	 */
	private void saveMetersXmlFileLastUpdateTime(long now ) throws Exception {
		URL cofu = XMLOrperator.class.getResource(AmConstant.meterConfigFileTimeFilePath);
		if(cofu == null){
			try {
				cofu = new URL(ResUtil.getAppPath(XMLOrperator.class,AmConstant.meterConfigFileTimeFilePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		if (cofu == null) {
			throw new Exception("出错！保存meter.xml最后更新时间的文件" + AmConstant.meterConfigFileTimeFilePath + "不存在。");
		}
		File cof = new File(cofu.getFile());
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(cof);
			o.write((now + "").getBytes());
			o.flush();
		} catch (Exception e) {
			throw new Exception("出错！将meter.xml最后更新时间写比较文件" + AmConstant.meterConfigFileTimeFilePath + "发生异常。");
		} finally {
			try {
				o.close();
			} catch (Exception ee) {
			} finally {
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////	
	//
	////////////////////////////////////////////////////////////////////////
	
	/**
	 * 增加新的测控器配置到配置文件中，
	 * 前提条件是，这个新测控器配置确实是新的，
	 * 即调用这个方法前已经与内存缓存的以前测控器配置
	 * 信息进行了比对。
	 * @param vo 新测控配置对象
	 * @throws Exception
	 */
	protected void addMeter(
			String id,
			String phone, 
			String satelliteId,
			String rtuProtocol,
			String sateProtocol,
			String channels,
			String mainChannel,
			String comId,
			String dataTo , 
			String onlineAlways,
			boolean updateXmlFile) throws Exception {
		XMLOrperator.updateMeterXmlFileToMemory = updateXmlFile ;
		synchronized(synObj){
			MeterConfig cc = new MeterConfig() ;
			Document doc = cc.createDomReDoc(AmConstant.meterConfigFilePath) ;
			Element root = doc.getRootElement() ;
			
			Element meter = new Element(MeterConstant.meter_key) ;

			meter.setAttribute(MeterConstant.id_key, id) ;
			meter.setAttribute(MeterConstant.phone_key, phone) ;
			meter.setAttribute(MeterConstant.satelliteId_key, satelliteId==null?"":satelliteId) ;

			meter.setAttribute(MeterConstant.rtuProtocol_key, rtuProtocol) ;
			meter.setAttribute(MeterConstant.rtuKey_key, AmConstant.rtuDefaultPasswordKey.intValue() + "") ;
			meter.setAttribute(MeterConstant.rtuDefaultPassword_key, AmConstant.rtuDefaultPassword.intValue() + "") ;
			meter.setAttribute(MeterConstant.sateProtocol_key, sateProtocol==null?"":sateProtocol) ;

			meter.setAttribute(MeterConstant.channels_key, channels) ;
			meter.setAttribute(MeterConstant.mainChannel_key, mainChannel) ;
			meter.setAttribute(MeterConstant.comId_key, comId==null?"":comId) ;
			

			meter.setAttribute(MeterConstant.dataTo_key, dataTo) ;
			meter.setAttribute(MeterConstant.onlineAlways_key, onlineAlways) ;
			
			
			root.addContent("\t") ;
			root.addContent(meter) ;
			root.addContent("\n") ;
			
			this.saveXML(doc , AmConstant.meterConfigFilePath) ;
		}
	}
	/**
	 * 修改测控器配置
	 * 前提条件是，这个新测控器配置确实是有变化，
	 * 即调用这个方法前已经与内存缓存的以前测控器配置
	 * 信息进行了比对。
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected void editMeter(			
			String oldId,
			String id,
			String phone, 
			String satelliteId,
			String rtuProtocol,
			String sateProtocol,
			String channels,
			String mainChannel,
			String comId,
			String dataTo , 
			String onlineAlways,
			boolean updateXmlFile) {
		XMLOrperator.updateMeterXmlFileToMemory = updateXmlFile ;
		synchronized(synObj){
			MeterConfig cc = new MeterConfig() ;
			Document doc = cc.createDomReDoc(AmConstant.meterConfigFilePath) ;
			Element root = doc.getRootElement() ;
			List list = root.getChildren() ;
			Iterator it = list.iterator() ;
			Element meter = null ;
			String Ide = null ;

			boolean finded = false ;
			
			while(it.hasNext()){
				meter = (Element)it.next() ;
				if(meter != null){
					Ide = meter.getAttributeValue(MeterConstant.id_key) ;
					if(Ide != null && Ide.equals(oldId)){
						meter.setAttribute(MeterConstant.id_key, id) ;
						meter.setAttribute(MeterConstant.phone_key, phone) ;
						meter.setAttribute(MeterConstant.satelliteId_key, satelliteId==null?"":satelliteId) ;

						meter.setAttribute(MeterConstant.rtuProtocol_key, rtuProtocol) ;
						meter.setAttribute(MeterConstant.sateProtocol_key, sateProtocol==null?"":sateProtocol) ;
						
						meter.setAttribute(MeterConstant.channels_key, channels) ;
						meter.setAttribute(MeterConstant.mainChannel_key, mainChannel) ;
						meter.setAttribute(MeterConstant.comId_key, comId==null?"":comId) ;
						
						meter.setAttribute(MeterConstant.onlineAlways_key, onlineAlways) ;
						meter.setAttribute(MeterConstant.dataTo_key, dataTo) ;

						finded = true ;
						break ;
					}
				}
				if(finded){
					break ;
				}
			}
			this.saveXML(doc , AmConstant.meterConfigFilePath) ;
		}
	}
	
	/**
	 * 更新测控器配置
	 * 前提条件是，这个新测控器配置确实是有变化，
	 * 即调用这个方法前已经与内存缓存的以前测控器配置
	 * 信息进行了比对。
	 * @param oldId 原来配置的测控器 Id
	 * @param newId 新测控器 ID 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected void updateMeterId(String oldId, String newId, boolean updateXmlFile) {
		XMLOrperator.updateMeterXmlFileToMemory = updateXmlFile ;
		synchronized(synObj){
			MeterConfig cc = new MeterConfig() ;
			Document doc = cc.createDomReDoc(AmConstant.meterConfigFilePath) ;
			Element root = doc.getRootElement() ;
			List list = root.getChildren() ;
			Iterator it = list.iterator() ;
			Element e = null ;
			String id = null ;
			while(it.hasNext()){
				e = (Element)it.next() ;
				id = e.getAttributeValue(MeterConstant.id_key) ;
					if(id != null && id.equals(oldId)){
						e.setAttribute(MeterConstant.id_key, newId) ;
						break ;
					}
			}
			this.saveXML(doc , AmConstant.meterConfigFilePath) ;
		}
	}
	/**
	 * 更新设备id
	 * @param oldId
	 * @param newId
	 * @param updateXmlFile
	 */
	public boolean updateMeterId_2(String oldId, String newId, boolean updateXmlFile) {
		if(oldId != null && newId != null 
				&& oldId.equalsIgnoreCase(newId)){//无需更新
			return true;
		}
		XMLOrperator.updateMeterXmlFileToMemory = updateXmlFile ;
		synchronized(synObj){
			MeterConfig cc = new MeterConfig() ;
			Document doc = cc.createDomReDoc(AmConstant.meterConfigFilePath) ;
			Element root = doc.getRootElement() ;
			List list = root.getChildren() ;
			Iterator it = list.iterator() ;
			Element e = null ;
			String id = null ;
			//验证新id是否存在
			boolean exit = false;
			while(it.hasNext()){
				e = (Element)it.next() ;
				id = e.getAttributeValue(MeterConstant.id_key) ;
					if(id != null && id.equals(newId)){
						exit = true;
						break ;
					}
			}
			if(exit){
				return false;
			}
			it = list.iterator();
			e = null;
			id = null;
			while(it.hasNext()){
				e = (Element)it.next() ;
				id = e.getAttributeValue(MeterConstant.id_key) ;
					if(id != null && id.equals(oldId)){
						e.setAttribute(MeterConstant.id_key, newId) ;
						break ;
					}
			}
			saveXML(doc , AmConstant.meterConfigFilePath) ;
			
			return true;
		}
	}

	/**
	 * 删除测控器配置
	 * @param rtuId
	 * @param updateXmlFile
	 */
	@SuppressWarnings("unchecked")
	protected void deleteMeter(String id, boolean updateXmlFile)throws Exception{
		XMLOrperator.updateMeterXmlFileToMemory = updateXmlFile ;
			synchronized(synObj){
			MeterConfig cc = new MeterConfig() ;
			Document doc = cc.createDomReDoc(AmConstant.meterConfigFilePath) ;
			Element root = doc.getRootElement() ;
			List list = root.getChildren() ;
			Iterator it = list.iterator() ;
			Element e = null ;
			String id2 = null ;
			while(it.hasNext()){
				e = (Element)it.next() ;
				id2 = e.getAttributeValue(MeterConstant.id_key) ;
				if(id2.equals(id)){
					root.removeContent(e) ;
					break ;
				}
			}
			this.saveXML(doc , AmConstant.meterConfigFilePath) ;
		}
	}
	/**
	 * 保存meters.xml文档
	 * 同时更新比较文保存的meters.xml的最后更新时间，
	 * 这样使检查meters.xml是否手工更新过的工作调度
	 * 任务不会误认为通过系统的更新为手工更新
	 * @param doc
	 * @param xmlFile
	 */
	private void saveXML(Document doc, String filePath) {
		URL fu = XMLOrperator.class.getResource(filePath);
		if(fu == null){
			try {
				fu = new URL(ResUtil.getAppPath(XMLOrperator.class,filePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		File file = new File(fu.getFile()) ;
		try {
			XMLOutputter outp = new XMLOutputter();
			outp.output(doc, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
			Logger log = LogManager.getLogger(XMLOrperator.class.getName());
			log.error("将测控器配置数据存入配置文件时发生异常！" + e.getMessage());
		}
	}
}

