package com.am.cs12.commu.core.remoteStatus.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.am.util.*;
import com.am.cs12.util.Config;
import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.am.cs12.commu.core.remoteStatus.MeterStatusConstant;

public class StatusConfig extends Config{

	/**
	 * 分析参数
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String , MeterStatus> parseOptions(String filePath) throws IllegalArgumentException {
		
		String now = DateTime.yyyyMMdd() ;
		
		Element root = super.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}
		
		HashMap<String , MeterStatus> hm = new HashMap<String , MeterStatus>() ;
		List list = root.getChildren();
		Iterator it = list.iterator();
		Element e = null;
		List list1 = null;
		Iterator it1 = null;
		Element e1 = null;
		String name = null ;
		String value = null ;
		
		MeterStatus vo = null ;
		
		boolean sameDate = false ;
		while (it.hasNext()) {
			sameDate = false ;
			vo = new MeterStatus() ;
			e = (Element) it.next();
			value = e.getAttributeValue(MeterStatusConstant.XMLRTUID) ;
			if(value == null || value.trim().equals("")){
				throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLRTUID + "被修改成空，这是不允许的！");
			}
			vo.id = value ;
			
//			value = e.getAttributeValue(MeterStatusConstant.XMLRTUPHONE) ;
//			if(value == null || value.trim().equals("")){
//				throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLRTUPHONE + "被修改成空，这是不允许的！");
//			}
//			vo.phone = value ;
//			
			
			value = e.getAttributeValue(MeterStatusConstant.XMLWORKMODEL) ;
			if(value == null){
				throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLWORKMODEL + "未配置，这是不允许的！");
			}
			vo.workModel = value ;
			
			value = e.getAttributeValue(MeterStatusConstant.XMLDATE) ;
			if(value == null){
				throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLDATE + "未配置，这是不允许的！");
			}
			vo.date = value.trim() ;

			if(vo.date.equals(now)){
				sameDate = true ;
			}else{
				vo.date = now ;
			}
			
			list1 = e.getChildren() ;
			it1 = list1.iterator() ;
			while(it1.hasNext()){
				e1 = (Element) it1.next() ;
				name = e1.getName() ;
				value = e1.getText() ;
				if(name.trim().equals(MeterStatusConstant.XMLON)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLON + "被修改成空，或不是合法整数，这是不允许的！");
					}
					vo.on = Long.parseLong(value) ;
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLONTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLONTD + "被修改成空，或不是合法整数，这是不允许的！");
						}
						vo.onTd = Long.parseLong(value) ;
					}
				}else{
					vo.onTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLOFF)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOFF + "被修改成空，或不是合法整数，这是不允许的！");
					}
					vo.off = Long.parseLong(value) ;
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLOFFTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOFFTD + "被修改成空，或不是合法整数，这是不允许的！");
						}
						vo.offTd = Long.parseLong(value) ;
					}
				}else{
					vo.offTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLSUCCESS)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLSUCCESS + "被修改成空，或不是合法整数，这是不允许的！");
					}
					vo.success = Long.parseLong(value) ;
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLSUCCESSTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLSUCCESSTD + "被修改成空，或不是合法整数，这是不允许的！");
						}
						vo.successTd = Long.parseLong(value) ;
					}
				}else{
					vo.successTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLFAIL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLSUCCESS + "被修改成空，或不是合法整数，这是不允许的！");
					}
					vo.fail = Long.parseLong(value) ;
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLFAILTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLSUCCESSTD + "被修改成空，或不是合法整数，这是不允许的！");
						}
						vo.failTd = Long.parseLong(value) ;
					}
				}else{
					vo.failTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLREPORT)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLREPORT + "被修改成空，或不是合法整数，这是不允许的！");
					}
					vo.report = Long.parseLong(value) ;
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLREPORTTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLREPORTTD + "被修改成空，或不是合法整数，这是不允许的！");
						}
						vo.reportTd = Long.parseLong(value) ;
					}
				}else{
					vo.reportTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLINTOTAL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLREPORT + "被修改成空，或不是合法整数，这是不允许的！");
					}else{
						vo.inTotal = Long.parseLong(value) ;
					}
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLINTOTALTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLREPORTTD + "被修改成空，或不是合法整数，这是不允许的！");
						}else{
							vo.inTotalTd = Long.parseLong(value) ;
						}
					}
				}else{
					vo.inTotalTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLOUTTOTAL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOUTTOTAL + "被修改成空，或不是合法实数，这是不允许的！");
					}else{
						vo.outTotal = Long.parseLong(value) ;
					}
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLOUTTOTALTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOUTTOTALTD + "被修改成空，或不是合法整数，这是不允许的！");
						}else{
							vo.outTotalTd = Long.parseLong(value) ;
						}
					}
				}else{
					vo.outTotalTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLOUTSMTOTAL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOUTSMTOTAL + "被修改成空，或不是合法整数，这是不允许的！");
					}else{
						vo.outSmTotal = Long.parseLong(value) ;
					}
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLOUTSMTOTALTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOUTSMTOTALTD + "被修改成空，或不是合法实数，这是不允许的！");
						}else{
							vo.outSmTotalTd = Long.parseLong(value) ;
						}
					}
				}else{
					vo.outSmTotalTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLINSMTOTAL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLINSMTOTAL + "被修改成空，或不是合法整数，这是不允许的！");
					}else{
						vo.inSmTotal = Long.parseLong(value) ;
					}
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLINSMTOTALTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLINSMTOTALTD + "被修改成空，或不是合法实数，这是不允许的！");
						}else{
							vo.inSmTotalTd = Long.parseLong(value) ;
						}
					}
				}else{
					vo.inSmTotalTd = 0 ;
				}
				
				
				if(name.trim().equals(MeterStatusConstant.XMLOUTSATETOTAL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOUTSATETOTAL + "被修改成空，或不是合法整数，这是不允许的！");
					}else{
						vo.outSateTotal = Long.parseLong(value) ;
					}
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLOUTSATETOTALTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLOUTSATETOTALTD + "被修改成空，或不是合法实数，这是不允许的！");
						}else{
							vo.outSateTotalTd = Long.parseLong(value) ;
						}
					}
				}else{
					vo.outSateTotalTd = 0 ;
				}
				if(name.trim().equals(MeterStatusConstant.XMLINSATETOTAL)){
					if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
						throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLINSATETOTAL + "被修改成空，或不是合法整数，这是不允许的！");
					}else{
						vo.inSateTotal = Long.parseLong(value) ;
					}
				}
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLINSATETOTALTD)){
						if(value == null || value.trim().equals("") || !NumberUtil.isIntNumber(value)){
							throw new IllegalArgumentException(filePath + "中元素" + MeterStatusConstant.XMLINSATETOTALTD + "被修改成空，或不是合法实数，这是不允许的！");
						}else{
							vo.inSateTotalTd = Long.parseLong(value) ;
						}
					}
				}else{
					vo.inSateTotalTd = 0 ;
				}
				
	
				
				if(sameDate){
					if(name.trim().equals(MeterStatusConstant.XMLREPORTTIME)){
						vo.reportTime = value ;
				}
				}else{
					vo.reportTime = "" ;
				}
			}
			vo.onLining = false ;
			hm.put(vo.id , vo);
		}
		return hm ;
	}

}
