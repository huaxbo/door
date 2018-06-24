package com.am.cs12.commu.core.remoteStatus.watch;

import java.util.*;
import com.am.common.timerTask.* ;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.am.util.DateTime;

/**
 * 此类是任务调度的工作类，每天在指定时间清空当天的状态数据时刻
 * @author Administrator
 *
 */
public class DailyWatcher extends ACTaskJob {

	/**
	 * 每天在指定时间清空当天的状态数据时刻
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws Exception {
		MeterStatusManager.instance() ;
		Set<Map.Entry<String , MeterStatus>> statusEntry = MeterStatusManager.id_status.entrySet() ;
		Iterator statusIt = statusEntry.iterator() ;
		Map.Entry<String , MeterStatus> entry = null ;
		MeterStatus svo = null ;
		while(statusIt.hasNext()){
			entry = (Map.Entry<String , MeterStatus>)statusIt.next() ;
			svo = entry.getValue() ;
			svo.date = DateTime.yyyyMMdd() ;
			svo.offTd = 0 ;
			svo.onTd =  0 ;
			svo.successTd = 0  ;
			svo.failTd = 0  ;
			svo.reportTd = 0  ;
			svo.inTotalTd = 0 ;
			svo.outTotalTd = 0 ;
			svo.outSmTotalTd = 0 ;
			svo.inSmTotalTd = 0 ;
			svo.outSateTotalTd = 0 ;
			svo.inSateTotalTd = 0 ;
			svo.reportTime = "" ;
		}
	}

}
