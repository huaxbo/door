package com.am.cs12.commu.core.remoteStatus.operate;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.am.cs12.commu.core.remoteStatus.config.StatusConfig;
import com.am.cs12.commu.core.remoteStatus.* ;
import com.am.util.ResUtil;


public class StatusXml {

	
	private static final Object synObj = new Object() ;
	
	/**
	 * 增加测控终端状态到状态集合
	 * @param vo
	 * @param statusFilePath
	 * @throws Exception
	 */
	public void addMeterStatus(MeterStatus vo , String statusFilePath)throws Exception {
		StatusConfig cc = new StatusConfig() ;
		Document doc = cc.createDomReDoc(statusFilePath) ;
		Element root = doc.getRootElement() ;
		Element meter = new Element(MeterStatusConstant.XMLMETER) ;
		meter.setAttribute(MeterStatusConstant.XMLRTUID, vo.id) ;
//		meter.setAttribute(MeterStatusConstant.XMLRTUPHONE, vo.phone) ;
		meter.setAttribute(MeterStatusConstant.XMLWORKMODEL, vo.workModel==null?"":vo.workModel) ;
		meter.setAttribute(MeterStatusConstant.XMLDATE, vo.date==null?"":vo.date) ;
		
		Element on = new Element(MeterStatusConstant.XMLON) ;
		on.setText(vo.on + "") ;
		Element onTd = new Element(MeterStatusConstant.XMLONTD) ;
		onTd.setText(vo.onTd + "") ;
		Element off = new Element(MeterStatusConstant.XMLOFF) ;
		off.setText(vo.off + "") ;
		Element offTd = new Element(MeterStatusConstant.XMLOFFTD) ;
		offTd.setText(vo.offTd + "") ;
		Element success = new Element(MeterStatusConstant.XMLSUCCESS) ;
		success.setText(vo.success + "") ;
		Element successTd = new Element(MeterStatusConstant.XMLSUCCESSTD) ;
		successTd.setText(vo.successTd + "") ;
		Element fail = new Element(MeterStatusConstant.XMLFAIL) ;
		fail.setText(vo.fail + "") ;
		Element failTd = new Element(MeterStatusConstant.XMLFAILTD) ;
		failTd.setText(vo.failTd + "") ;
		Element report = new Element(MeterStatusConstant.XMLREPORT) ;
		report.setText(vo.report + "") ;
		Element reportTd = new Element(MeterStatusConstant.XMLREPORTTD) ;
		reportTd.setText(vo.reportTd + "") ;
		Element inTotal = new Element(MeterStatusConstant.XMLINTOTAL) ;
		inTotal.setText(vo.inTotal + "") ;
		Element inTotalTd = new Element(MeterStatusConstant.XMLINTOTALTD) ;
		inTotalTd.setText(vo.inTotalTd + "") ;
		Element outTotal = new Element(MeterStatusConstant.XMLOUTTOTAL) ;
		outTotal.setText(vo.outTotal + "") ;
		Element outTotalTd = new Element(MeterStatusConstant.XMLOUTTOTALTD) ;
		outTotalTd.setText(vo.outTotalTd + "") ;

		Element outSmTotal = new Element(MeterStatusConstant.XMLOUTSMTOTAL) ;
		outSmTotal.setText(vo.outSmTotal + "") ;
		Element outSmTotalTd = new Element(MeterStatusConstant.XMLOUTSMTOTALTD) ;
		outSmTotalTd.setText(vo.outSmTotalTd + "") ;
		Element inSmTotal = new Element(MeterStatusConstant.XMLINSMTOTAL) ;
		inSmTotal.setText(vo.inSmTotal + "") ;
		Element inSmTotalTd = new Element(MeterStatusConstant.XMLINSMTOTALTD) ;
		inSmTotalTd.setText(vo.inSmTotalTd + "") ;

		Element outSateTotal = new Element(MeterStatusConstant.XMLOUTSATETOTAL) ;
		outSateTotal.setText(vo.outSateTotal + "") ;
		Element outSateTotalTd = new Element(MeterStatusConstant.XMLOUTSATETOTALTD) ;
		outSateTotalTd.setText(vo.outSateTotalTd + "") ;
		Element inSateTotal = new Element(MeterStatusConstant.XMLINSATETOTAL) ;
		inSateTotal.setText(vo.inSateTotal + "") ;
		Element inSateTotalTd = new Element(MeterStatusConstant.XMLINSATETOTALTD) ;
		inSateTotalTd.setText(vo.inSateTotalTd + "") ;
		
		Element reportTime = new Element(MeterStatusConstant.XMLREPORTTIME) ;
		reportTime.setText(vo.reportTime + "") ;
		
		root.addContent("\n\t") ;
		root.addContent(meter) ;

		meter.addContent("\n\t\t") ;
		meter.addContent(on) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(onTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(off) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(offTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(success) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(successTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(fail) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(failTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(report) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(reportTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(inTotal) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(inTotalTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(outTotal) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(outTotalTd) ;
		meter.addContent("\n\t\t") ;
		meter.addContent(outSmTotal);
		meter.addContent("\n\t\t");
		meter.addContent(outSmTotalTd);
		meter.addContent("\n\t\t");
		meter.addContent(inSmTotal);
		meter.addContent("\n\t\t");
		meter.addContent(inSmTotalTd);

		meter.addContent("\n\t\t") ;
		meter.addContent(outSateTotal);
		meter.addContent("\n\t\t");
		meter.addContent(outSateTotalTd);
		meter.addContent("\n\t\t");
		meter.addContent(inSateTotal);
		meter.addContent("\n\t\t");
		meter.addContent(inSateTotalTd);
		
		meter.addContent("\n\t\t");
		meter.addContent(reportTime) ;
		meter.addContent("\n\t") ;
//		root.addContent("\n") ;
		this.saveXML(doc , statusFilePath) ;
	}
	/**
	 * 删除测控终端状态
	 * @param id
	 * @param statusFilePath
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void removeMeterStatus(String id , String statusFilePath)throws Exception {
		StatusConfig cc = new StatusConfig() ;
		Document doc = cc.createDomReDoc(statusFilePath) ;
		Element root = doc.getRootElement() ;
		List list = root.getChildren() ;
		Iterator it = list.iterator() ;
		Element e = null ;
		String rtuId = null ;
		while(it.hasNext()){
			e = (Element)it.next() ;
			rtuId = e.getAttributeValue(MeterStatusConstant.XMLRTUID) ;
			if(rtuId.equals(id)){
				root.removeContent(e) ;
				break ;
			}
		}
		this.saveXML(doc , statusFilePath) ;
	}
	
	/**
	 * 更新控终端的ID
	 * @param newId
	 * @param oldId
	 * @param statusFilePath
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMeterId(String newId , String oldId , String statusFilePath)throws Exception {
		StatusConfig cc = new StatusConfig() ;
		Document doc = cc.createDomReDoc(statusFilePath) ;
		Element root = doc.getRootElement() ;
		List list = root.getChildren() ;
		Iterator it = list.iterator() ;
		Element e = null ;
		String rtuId = null ;
		while(it.hasNext()){
			e = (Element)it.next() ;
			rtuId = e.getAttributeValue(MeterStatusConstant.XMLRTUID) ;
			if(rtuId.equals(oldId)){
				e.setAttribute(MeterStatusConstant.XMLRTUID, newId) ;
				break ;
			}
		}
		this.saveXML(doc , statusFilePath) ;
	}
	
	/**
	 * 将内存中缓存的测控终端状态持久化到xml文件中
	 * @param vos
	 * @param statusFilePath
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveMeterStatus(HashMap<String, MeterStatus> vos , String statusFilePath)throws Exception {
		StatusConfig cc = new StatusConfig() ;
		Document doc = cc.createDomReDoc(statusFilePath) ;
		Element root = doc.getRootElement() ;
		root.removeContent() ;
		
		Set<Map.Entry<String , MeterStatus>> statusEntry = vos.entrySet() ;
		Iterator statusIt = statusEntry.iterator() ;
		Map.Entry<String , MeterStatus> entry = null ;
		MeterStatus vo = null ;
		while (statusIt.hasNext()) {
			entry = (Map.Entry<String, MeterStatus>) statusIt.next();
			vo = entry.getValue();

			Element meter = new Element(MeterStatusConstant.XMLMETER);
			meter.setAttribute(MeterStatusConstant.XMLRTUID, vo.id);
//			meter.setAttribute(MeterStatusConstant.XMLRTUPHONE, vo.phone) ;
			meter.setAttribute(MeterStatusConstant.XMLWORKMODEL, vo.workModel==null?"":vo.workModel) ;
			meter.setAttribute(MeterStatusConstant.XMLDATE, vo.date==null?"":vo.date) ;

			Element on = new Element(MeterStatusConstant.XMLON);
			on.setText(vo.on + "");
			Element off = new Element(MeterStatusConstant.XMLOFF);
			off.setText(vo.off + "");
			Element success = new Element(MeterStatusConstant.XMLSUCCESS);
			success.setText(vo.success + "");
			Element fail = new Element(MeterStatusConstant.XMLFAIL);
			fail.setText(vo.fail + "");
			Element report = new Element(MeterStatusConstant.XMLREPORT);
			report.setText(vo.report + "");
			Element inTotal = new Element(MeterStatusConstant.XMLINTOTAL);
			inTotal.setText(vo.inTotal + "");
			Element outTotal = new Element(MeterStatusConstant.XMLOUTTOTAL);
			outTotal.setText(vo.outTotal + "");

			Element outSmTotal = new Element(MeterStatusConstant.XMLOUTSMTOTAL);
			outSmTotal.setText(vo.outSmTotal + "");
			Element inSmTotal = new Element(MeterStatusConstant.XMLINSMTOTAL);
			inSmTotal.setText(vo.inSmTotal + "");

			Element outSateTotal = new Element(MeterStatusConstant.XMLOUTSATETOTAL);
			outSateTotal.setText(vo.outSateTotal + "");
			Element inSateTotal = new Element(MeterStatusConstant.XMLINSATETOTAL);
			inSateTotal.setText(vo.inSateTotal + "");
			
			Element reportTime = new Element(MeterStatusConstant.XMLREPORTTIME);
			reportTime.setText(vo.reportTime);

			
			Element onTd = new Element(MeterStatusConstant.XMLONTD);
			onTd.setText(vo.onTd + "");
			Element offTd = new Element(MeterStatusConstant.XMLOFFTD);
			offTd.setText(vo.offTd + "");
			Element successTd = new Element(MeterStatusConstant.XMLSUCCESSTD);
			successTd.setText(vo.successTd + "");
			Element failTd = new Element(MeterStatusConstant.XMLFAILTD);
			failTd.setText(vo.failTd + "");
			Element reportTd = new Element(MeterStatusConstant.XMLREPORTTD);
			reportTd.setText(vo.reportTd + "");
			Element inTotalTd = new Element(MeterStatusConstant.XMLINTOTALTD);
			inTotalTd.setText(vo.inTotalTd + "");
			Element outTotalTd = new Element(MeterStatusConstant.XMLOUTTOTALTD);
			outTotalTd.setText(vo.outTotalTd + "");
			
			Element outSmTotalTd = new Element(MeterStatusConstant.XMLOUTSMTOTALTD);
			outSmTotalTd.setText(vo.outSmTotalTd + "");
			Element inSmTotalTd = new Element(MeterStatusConstant.XMLINSMTOTALTD);
			inSmTotalTd.setText(vo.inSmTotalTd + "");
			
			Element outSateTotalTd = new Element(MeterStatusConstant.XMLOUTSATETOTALTD);
			outSateTotalTd.setText(vo.outSateTotalTd + "");
			Element inSateTotalTd = new Element(MeterStatusConstant.XMLINSATETOTALTD);
			inSateTotalTd.setText(vo.inSateTotalTd + "");

			root.addContent("\t");
			root.addContent(meter);

			meter.addContent("\n\t\t");
			meter.addContent(on);
			meter.addContent("\n\t\t");
			meter.addContent(onTd);
			meter.addContent("\n\t\t");
			meter.addContent(off);
			meter.addContent("\n\t\t");
			meter.addContent(offTd);
			meter.addContent("\n\t\t");
			meter.addContent(success);
			meter.addContent("\n\t\t");
			meter.addContent(successTd);
			meter.addContent("\n\t\t");
			meter.addContent(fail);
			meter.addContent("\n\t\t");
			meter.addContent(failTd);
			meter.addContent("\n\t\t");
			meter.addContent(report);
			meter.addContent("\n\t\t");
			meter.addContent(reportTd);
			meter.addContent("\n\t\t");
			meter.addContent(inTotal);
			meter.addContent("\n\t\t");
			meter.addContent(inTotalTd);
			meter.addContent("\n\t\t");
			meter.addContent(outTotal);
			meter.addContent("\n\t\t");
			meter.addContent(outTotalTd);
			meter.addContent("\n\t\t");
			
			meter.addContent(outSmTotal);
			meter.addContent("\n\t\t");
			meter.addContent(outSmTotalTd);
			meter.addContent("\n\t\t");
			meter.addContent(inSmTotal);
			meter.addContent("\n\t\t");
			meter.addContent(inSmTotalTd);
			meter.addContent("\n\t\t");
			
			meter.addContent(outSateTotal);
			meter.addContent("\n\t\t");
			meter.addContent(outSateTotalTd);
			meter.addContent("\n\t\t");
			meter.addContent(inSateTotal);
			meter.addContent("\n\t\t");
			meter.addContent(inSateTotalTd);
			meter.addContent("\n\t\t");
			
			meter.addContent(reportTime);
			meter.addContent("\n\t");
		
			root.addContent("\n");

		}
		this.saveXML(doc , statusFilePath) ;
	}
	/**
	 * 保存meterStatus.xml文档
	 * 同时更新比较文保存的meterStatus.xml的最后更新时间，
	 * 这样使检查meterStatus.xml是否手工更新过的工作调度
	 * 任务不会误认为通过系统的更新为手工更新
	 * @param doc
	 * @param xmlFile
	 */
	private void saveXML(Document doc, String statusFilePath) {
		synchronized(synObj){			
			URL fu = StatusXml.class.getResource(statusFilePath);
			if(fu == null){
				try {
					fu = new URL(ResUtil.getAppPath(StatusXml.class,statusFilePath));
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
				Logger log = LogManager.getLogger(StatusXml.class.getName());
				log.error("将测控终端状态数据存入持久化文件时发生异常！" + e.getMessage());
			}
		}

	}
	

}
