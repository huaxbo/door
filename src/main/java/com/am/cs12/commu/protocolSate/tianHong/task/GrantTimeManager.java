package com.am.cs12.commu.protocolSate.tianHong.task;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import java.util.Map.Entry;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.SateProtocolVO;
import com.am.cs12.config.SerialPortVO;
import com.am.common.timerTask.ACSchedularJob;
import org.apache.logging.log4j.Logger;

public class GrantTimeManager {
	
	private static Logger log = LogManager.getLogger(GrantTimeManager.class.getName()) ;
	
	private static GrantTimeManager instance ;
	
	private static Boolean startedGrantTime = false ;
	
	private static int startHour = 12 ;
	private static int startMinute = 30 ;
	
	private static final String job = "GrantTimeJob" ;
	private static final String jobGroup = "GrantTimeJobGroup" ;
	
	
	private GrantTimeManager(){} ;
	
	public static GrantTimeManager instance(){
		if(instance == null){
			instance = new GrantTimeManager() ;
		}
		return instance ;
	}

	/**
	 * 启动授时任务
	 * @param protocolName 卫星协议名称
	 */
	public void startGrantTimeTask(String protocolName){
		synchronized (startedGrantTime) {
			if(!startedGrantTime){
				startedGrantTime = true ;
				try {
					
					ConfigCenter cc = ConfigCenter.instance() ;
					
					HashMap<String, SateProtocolVO> smap = cc.getSateProtocolMap() ;
					if(smap == null || smap.size() == 0){
						return ;
					}
					SateProtocolVO svo = (SateProtocolVO)smap.get(protocolName) ;
					if(svo == null){
						return ;
					}
					startHour = svo.grantTimeStartHour ;
					startMinute = svo.grantTimeStartMinute ;
					
					HashMap<String, SerialPortVO> pmap = cc.getSerialPortMap() ;
					if(pmap == null || pmap.size() == 0){
						return ;
					}
					Set<Entry<String, SerialPortVO>> set = pmap.entrySet() ;
					Iterator<Entry<String, SerialPortVO>> it = set.iterator() ;
					Entry<String, SerialPortVO> entry = null ;
					SerialPortVO vo = null ;
					String comId = null ;
					while(it.hasNext()){
						entry = it.next() ;
						vo = entry.getValue() ;
						if(vo.getProtocolName().equals(protocolName)){
							comId = vo.getId() ;
							break ;
						}
					}
					if(comId == null){
						return ;
					}
					
					HashMap<String , Object> jobDataMap = new HashMap<String , Object>() ;
					jobDataMap.put("code", CodeTH.$TAPP) ;
					jobDataMap.put("comId", comId) ;
					jobDataMap.put("protocolName", protocolName) ;
					
					
					new ACSchedularJob().addDailyJob(
							job , 
							jobGroup , 
							GrantTimeTask.class, 
							jobDataMap ,
							startHour,//0时，必须在0时后启动，因为要更改状态对象的日期属性，只有在0时后，才能将其改为当日
							startMinute//1分 启动
							) ;
					log.info("启动了卫星校时任务(执行校时时刻" + startHour + ":" + startMinute + ")。") ;
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
