package com.am.cs12.commu.core.innerCommand.help;

import java.util.*;

import com.am.cs12.commu.protocol.amLocal.* ;
import com.am.cs12.commu.core.remoteStatus.MeterStatusDataProvider;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.MeterVO;

public class HelpOnLine {

	/**
	 * 查询在线与不在线情况
	 * @return
	 */
	public OnLine onLine(){
		OnLine ol = new OnLine() ;
		MeterStatusDataProvider pro = new MeterStatusDataProvider() ;
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters != null){
			String id = null ;
			Set<String> set = meters.keySet() ;
			Iterator<String> it = set.iterator() ;
			while(it.hasNext()){
				id = it.next() ;
				if(pro.isOnLine(id)){
					ol.setOnLine(id, true) ;
				}else{
					ol.setOnLine(id, false) ;
				}
			}
			
		}
		return ol ;
	}

	/**
	 * 查询在线情况
	 * @return
	 * @throws Exception
	 */
	public OnLine onLineYes() throws Exception{
		OnLine ol = new OnLine() ;
		MeterStatusDataProvider pro = new MeterStatusDataProvider() ;
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters != null){
			String id = null ;
			Set<String> set = meters.keySet() ;
			Iterator<String> it = set.iterator() ;
			while(it.hasNext()){
				id = it.next() ;
				if(pro.isOnLine(id)){
					ol.setOnLine(id, true) ;
				}
			}
			
		}
		return ol ;
	}
	
	
	/**
	 * 查询不在线情况
	 * @return
	 * @throws Exception
	 */
	public OnLine onLineNo() throws Exception{
		OnLine ol = new OnLine() ;
		MeterStatusDataProvider pro = new MeterStatusDataProvider() ;
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters != null){
			String id = null ;
			Set<String> set = meters.keySet() ;
			Iterator<String> it = set.iterator() ;
			while(it.hasNext()){
				id = it.next() ;
				if(!pro.isOnLine(id)){
					ol.setOnLine(id, false) ;
				}
			}
			
		}
		return ol ;
	}
	
	
	/**
	 * 查询在线情况
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public OnLine onLineSome(String[] ids) throws Exception {
		OnLine ol = new OnLine();
		MeterStatusDataProvider pro = new MeterStatusDataProvider() ;
		String id = null;
		for (int i = 0; i < ids.length; i++) {
			id = ids[i];
			if (pro.isOnLine(id)) {
				ol.setOnLine(id, true);
			} else {
				ol.setOnLine(id, false);
			}
		}

		return ol;
	}

}
