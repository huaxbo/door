package com.am.cs12.commu.core.remoteStatus;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import com.am.util.DateTime;
import com.am.common.timerTask.ACSchedularJob;
import com.am.cs12.commu.core.remoteStatus.config.StatusConfig;
import com.am.cs12.commu.core.remoteStatus.operate.StatusXml;
import com.am.cs12.commu.core.remoteStatus.watch.DailyWatcher;
import com.am.cs12.commu.core.remoteStatus.watch.MonitorWatcher;
import com.am.cs12.commu.core.remoteStatus.watch.SaveWatcher;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.MeterVO;
import com.am.cs12.util.AmConstant;


public class MeterStatusManager {
	private static Logger log = LogManager.getLogger(MeterStatusManager.class.getName()) ;
	//测控终端状态
	public static HashMap<String , MeterStatus> id_status = new HashMap<String , MeterStatus>() ;
	//监控视测控终端状态工作间隔时长(分钟)(必须是偶数)
	public static int monitorMeterStatusInterval = 2 ;
	
	private static MeterStatusManager instance ;
	
	private static boolean startedJob = false ;
	
	/**
	 * 私有构造方法
	 */
	private MeterStatusManager(){
		StatusConfig sc = new StatusConfig() ;
		sc.createDom(AmConstant.meterStatusFilePath) ;
		id_status = sc.parseOptions(AmConstant.meterStatusFilePath) ;
		this.compare();
	}
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static MeterStatusManager instance(){
		if(instance == null){
			instance = new MeterStatusManager() ;
		}
		return instance ;
	}
	
	/**
	 * 启动观察者
	 * @throws Exception
	 */
	public void startSchedularJobs() throws Exception{
		if(!startedJob){
			//初始化观察者，以完成定时工作作任务
			//每天在指定时间清空当天的状态数据时刻
			new ACSchedularJob().addDailyJob(
					MeterStatusConstant.JOB_DAILYWATCHERNAME , 
					MeterStatusConstant.JOB_GROUPNAME , 
					DailyWatcher.class, 
					null ,
					0,//0时，必须在0时后启动，因为要更改状态对象的日期属性，只有在0时后，才能将其改为当日
					1//1分 启动
					) ;
			//检测并更新测控终端在线情况及在线时长和不在线时长
			if(monitorMeterStatusInterval%2 != 0){
				monitorMeterStatusInterval ++ ;
			}
			new ACSchedularJob().addMinutelyJob(
					MeterStatusConstant.JOB_REFRESHWATCHERNAME , 
					MeterStatusConstant.JOB_GROUPNAME , 
					MonitorWatcher.class, 
					null ,
					10000,
					monitorMeterStatusInterval, //隔monitorMeterStatusInterval分钟更新一次
					-1//永不停息
					) ;
			//将内存缓存的测控终端状态持久化到xml存储文件中
			new ACSchedularJob().addMinutelyJob(
					MeterStatusConstant.JOB_SAVEWATCHERNAME , 
					MeterStatusConstant.JOB_GROUPNAME , 
					SaveWatcher.class, 
					null ,
					10000,
					5, //隔5分钟持久化一次
					-1//永不停息
					) ;
		}
		startedJob = true ;
	}
	

	/**
	 * 把测控终端与config/meters.xml中配置的Meter进行比对 进行Meter增减操作
	 */
	@SuppressWarnings("unchecked")
	private void compare() {
		
		Map<String, MeterVO> rtuId2meter = ConfigCenter.instance().getId_meterMap() ;
		
		Set<Map.Entry<String, MeterStatus>> statusEntry = id_status.entrySet();
		Iterator statusIt = statusEntry.iterator();
		Map.Entry<String, MeterStatus> entry = null;
		String key = null;
		MeterStatus svo = null;
		MeterVO cvo = null;
		while (statusIt.hasNext()) {
			entry = (Map.Entry<String, MeterStatus>) statusIt.next();
			key = entry.getKey();
			svo = entry.getValue();
			cvo = (MeterVO) rtuId2meter.get(key);
			if (cvo == null) {
				// 说明在meter.xml删除一个Meter配置
				try {
					this.removeMeterStatusToXml(svo.id, AmConstant.meterStatusFilePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				statusIt.remove();
			} 
		}

		Set<Map.Entry<String, MeterVO>> metersEntry = rtuId2meter.entrySet();
		Iterator meterIt = metersEntry.iterator();
		Map.Entry<String, MeterVO> meterEntry = null;
		while (meterIt.hasNext()) {
			meterEntry = (Map.Entry<String, MeterVO>) meterIt.next();
			key = meterEntry.getKey();
			cvo = meterEntry.getValue();
			svo = (MeterStatus) id_status.get(key);
			if (svo == null) {
				// 说明meter.xml中新增加的Meter，在状态中还不存在它
				MeterStatus vo = new MeterStatus();
				vo.id = cvo.id;
//				vo.phone = cvo.rtu.phone;
				vo.date = DateTime.yyyyMMdd() ;
				vo.on = 0;
				vo.off = 0;
				vo.onTd = 0;
				vo.offTd = 0;
				vo.success = 0;
				vo.fail = 0;
				vo.report = 0;
				vo.inTotal = 0;
				vo.outTotal = 0;
				vo.outSmTotal = 0;
				vo.inSmTotal = 0;
				vo.outSateTotal = 0;
				vo.inSateTotal = 0;
				vo.successTd = 0;
				vo.failTd = 0;
				vo.reportTd = 0;
				vo.inTotalTd = 0;
				vo.outTotalTd = 0;
				vo.outSmTotalTd = 0;
				vo.inSmTotalTd = 0;
				vo.outSateTotalTd = 0;
				vo.inSateTotalTd = 0;
				vo.reportTime = "";
				try {
					this.addMeterStatusToXml(vo, AmConstant.meterStatusFilePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				id_status.put(key, vo);
			}
		}

	}


	/**
	 * 增加Meter新配置到状态集合
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void addMeter(String id) {
		if (!id_status.containsKey(id)) {
			MeterStatus vo = new MeterStatus();
			vo.id = id;
			vo.workModel = "" ;
			vo.date = DateTime.yyyyMMdd() ;
			vo.on = 0;
			vo.off = 0;
			vo.onTd = 0;
			vo.offTd = 0;
			vo.success = 0;
			vo.fail = 0;
			vo.report = 0;
			vo.inTotal = 0;
			vo.outTotal = 0;
			vo.outSmTotal = 0;
			vo.inSmTotal = 0;
			vo.outSateTotal = 0;
			vo.inSateTotal = 0;
			vo.successTd = 0;
			vo.failTd = 0;
			vo.reportTd = 0;
			vo.inTotalTd = 0;
			vo.outTotalTd = 0;
			vo.outSmTotalTd = 0;
			vo.inSmTotalTd = 0;
			vo.outSateTotalTd = 0;
			vo.inSateTotalTd = 0;
			vo.reportTime = "";
			this.addMeterStatusToXml(vo, AmConstant.meterStatusFilePath);
			id_status.put(id, vo) ;
		}
	}
	/**
	 * 编辑Meter新配置到状态集合
	 * 
	 * @param oldRtuId
	 * @param id
	 * @throws Exception
	 */
	public void editMeter(String oldId , String id) {
		if (id_status.containsKey(oldId)) {
			this.editMeterStatusToXml(id, oldId, AmConstant.meterStatusFilePath) ;
			MeterStatus vo = id_status.get(oldId) ;
			if(vo != null){
				vo.id = id ;
				id_status.remove(oldId) ;
				id_status.put(id, vo) ;
			}
		}
	}
	/**
	 * 删除测控终端
	 * 
	 * @param rtuId
	 * @throws Exception
	 */
	public void removeMeter(String rtuId) {
		id_status.remove(rtuId);
		this.removeMeterStatusToXml(rtuId , AmConstant.meterStatusFilePath);
	}
	/**
	 * 测控终端RTU ID更改
	 * @param newId
	 * @param oldId
	 */
	public void updateMeterId(String newId , String oldId) {
		if(id_status.containsKey(oldId)){
			MeterStatus vo = id_status.get(oldId) ;
			id_status.put(newId, vo) ;
			this.updateMeterRtuIdToXml(newId, oldId, AmConstant.meterStatusFilePath);
		}
	}

	/**
	 * 收到了测控终端主动上报数据
	 */
	public void atuoReportData(
			String rtuId ,//测控终端RTU ID
			Integer meterWorkModel //测控终端工作模式
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.report++;
			vo.reportTd++;
			if (vo.reportTime == null || vo.reportTime.trim().length() == 0) {
				vo.reportTime = DateTime.HH() + ":" + DateTime.mm();
			} else {
				vo.reportTime = vo.reportTime + "@" + DateTime.HH() + ":"
						+ DateTime.mm();
			}
			if(meterWorkModel != null){
//				工作模式类型=00B，设置遥测终端在兼容工作状态；
//				工作模式类型=01B，设置遥测终端在自报工作状态；
//				工作模式类型=02B，设置遥测终端在查询/应答工作状态；
//				工作模式类型=03B，遥测终端在调试/维修状态。
				if(meterWorkModel.intValue() == AmConstant.workModel_int_0.intValue()){
					vo.workModel = AmConstant.workModel_str_0 ; //"兼容" ;
				}else if(meterWorkModel.intValue() == AmConstant.workModel_int_1.intValue()){
					vo.workModel = AmConstant.workModel_str_1 ; //自报" ;
				}else if(meterWorkModel.intValue() == AmConstant.workModel_int_2.intValue()){
					vo.workModel = AmConstant.workModel_str_2 ; //"查询/应答" ;
				}else if(meterWorkModel.intValue() == AmConstant.workModel_int_3.intValue()){
					vo.workModel = AmConstant.workModel_str_3 ; //"调试/维修" ;
				}
			}
			log.info("记录主动上报数据状态，至当前主动上报各个时刻" + vo.reportTime) ;
		}
	}

	/**
	 * 收到了测控终端数据
	 */
	public void receivedMeterData(
			String rtuId ,//测控终端RTU ID
			String ip ,
			int port ,
			int dataLen//数据长度(字节数)
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.onLining = true ;
			vo.ip = ip ;
			vo.port = port ;
			vo.lastMeterDataMoment = System.currentTimeMillis() ;
			vo.inTotal += dataLen;
			vo.inTotalTd += dataLen;
		}
	}
	/**
	 * 向测控终端发送了数据
	 */
	public void sendMeterData(
			String rtuId ,//测控终端RTU ID
			int dataLen//数据长度(字节数)
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.outTotal += dataLen;
			vo.outTotalTd += dataLen;
		}
	}

	/**
	 * 收到了测控终端短信
	 */
	public void receivedMeterSM(
			String rtuId //测控终端RTU ID
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.inSmTotal++ ;
			vo.inSmTotalTd++ ;
		}
	}
	/**
	 * 向测控终端发送了短信
	 */
	public void sendMeterSM(
			String rtuId //测控终端RTU ID
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.outSmTotal++ ;
			vo.outSmTotalTd++ ;
		}
	}
	/**
	 * 收到了测控终端卫星数据
	 */
	public void receivedMeterSatellite(
			String rtuId //测控终端RTU ID
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.inSateTotal++ ;
			vo.inSateTotalTd++ ;
		}
	}
	/**
	 * 向测控终端发送了卫星数据
	 */
	public void sendMeterSatellite(
			String rtuId //测控终端RTU ID
			){
		MeterStatus vo = id_status.get(rtuId) ;
		if(vo == null){
			this.addMeter(rtuId) ;
		}else{
			vo.outSateTotal++ ;
			vo.outSateTotalTd++ ;
		}
	}


	/**
	 * 测控终端命令成功一次
	 * 
	 * @param rtuId
	 */
	public void commandSuccess(String rtuId) {
		MeterStatus vo = (MeterStatus) id_status.get(rtuId);
		if (vo != null) {
			vo.success++;
			vo.successTd++;
			vo.continueFailCommandTimes = 0 ;
		}
	}

	/**
	 * 测控终端命令失败一次
	 * 
	 * @param rtuId
	 */
	public void commandFail(String rtuId) {
		MeterStatus vo = (MeterStatus) id_status.get(rtuId);
		if (vo != null) {
			vo.fail++;
			vo.failTd++;
			vo.continueFailCommandTimes++ ;
		}
	}

	/**
	 * 测控终端命令失败转成功一次
	 * @param rtuId
	 */
	public void commandFail2Success(String rtuId) {
		MeterStatus vo = (MeterStatus) id_status.get(rtuId);
		if (vo != null) {
			vo.fail--;
			vo.success++;
			vo.failTd--;
			vo.successTd++;
			vo.continueFailCommandTimes = 0 ;
			if(vo.fail < 0){
				vo.fail = 0 ;
			}
			if(vo.failTd < 0){
				vo.failTd = 0 ;
			}
		}
	}
	
	
	
	////////////////////////////////////////////////////
	//私有方法
	////////////////////////////////////////////////////

	/**
	 * 增加测控终端到状态集合
	 * 
	 * @param vo
	 * @throws Exception
	 */
	private void addMeterStatusToXml(MeterStatus vo, String filePath) {
		try {
			new StatusXml().addMeterStatus(vo, filePath);
		} catch (Exception e) {
			log.error("在测控终端状态持久化文件中增加测控终端(RTU ID=" + vo.id + ")时出错：" + e.getMessage() , e) ;
		}
	}
	/**
	 * 编辑测控终端到状态集合
	 * 
	 * @param vo
	 * @throws Exception
	 */
	private void editMeterStatusToXml(String newRtuId , String oldRtuId , String filePath) {
		this.updateMeterRtuIdToXml(newRtuId, oldRtuId, filePath) ;
	}

	/**
	 * 删除测控终端
	 * 
	 * @param vo
	 * @throws Exception
	 */
	private void removeMeterStatusToXml(String id, String filePath) {
		try {
			new StatusXml().removeMeterStatus(id, filePath);
		} catch (Exception e) {
			log.error("从测控终端状态持久化文件中删除测控终端(ID=" + id + ")时出错：" + e.getMessage() , e) ;
		}
	}
	/**
	 * 更新控终端的RTU ID
	 * @param id 新RTU ID
	 * @param oldRtuId 原RTU ID
	 * @param statusFilePath
	 * @throws Exception
	 */
	private void updateMeterRtuIdToXml(String newId , String oldId , String statusFilePath) {
		try {
			new StatusXml().updateMeterId(newId, oldId, statusFilePath);
		} catch (Exception e) {
			log.error("测控终端状态持久化文件中更新测控终端状态(原ID=" + oldId + "新ID=" + newId + ")时出错：" + e.getMessage() , e) ;
		}
	}

}
