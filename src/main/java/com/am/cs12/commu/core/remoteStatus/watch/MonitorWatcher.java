package com.am.cs12.commu.core.remoteStatus.watch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.am.common.timerTask.ACTaskJob;
import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.config.ProtocolBaseVO;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;

public class MonitorWatcher extends ACTaskJob {

	private static Logger log = LogManager.getLogger(MonitorWatcher.class.getName()) ;

	/**
	 * 检测并更新测控终端在线情况及在线时长和不在线时长
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws Exception {
		Set<Map.Entry<String , MeterStatus>> statusEntry = MeterStatusManager.id_status.entrySet() ;
		Iterator statusIt = statusEntry.iterator() ;
		Map.Entry<String , MeterStatus> entry = null ;
		MeterStatus svo = null ;
		ProtocolBaseVO pb = ProtocolBaseVO.instance() ;
		int maxComFailTimes = pb.maxContinueFailCommandTimes ;
		int minConnIdelInterval = pb.minMeterConnectIdleInterval * 60 * 1000 ;
		boolean closeConn1 = pb.checkConnectOffCloseSocketForIdleInterval ;
		boolean closeConn2 = pb.checkConnectOffCloseSocketForContinueFailCommandTimes ;
		
		while(statusIt.hasNext()){
			entry = (Map.Entry<String , MeterStatus>)statusIt.next() ;
			svo = entry.getValue() ;
			if(svo.onLining){
				//当前在线
				if(System.currentTimeMillis() - svo.lastMeterDataMoment > minConnIdelInterval){				
					if(closeConn1){//无上传数据时长达到限值
						svo.onLining = false ;
						log.warn("无上传数据时长达到限值，网络连接将关闭。") ;
					}
				}
				if(svo.continueFailCommandTimes >= maxComFailTimes){
					//连续命令失败次数达到限值
					svo.continueFailCommandTimes = 0 ;
					if(closeConn2){
						svo.onLining = false ;
						log.warn("连续命令失败次数达到限值，网络连接将关闭。") ;
					}
				}
				if(!svo.onLining){
					//由在线转为不在线
					if(closeConn1 || closeConn2){
						//关闭网络连接
						RemoteSessionManager.instance().closeSession(svo.id , "测控终端状态观察者发现测控终端为非在线，") ;
					}
					svo.on += MeterStatusManager.monitorMeterStatusInterval/2 ;
					svo.onTd += MeterStatusManager.monitorMeterStatusInterval/2 ;
					svo.off += MeterStatusManager.monitorMeterStatusInterval/2 ;
					svo.offTd += MeterStatusManager.monitorMeterStatusInterval/2 ;
				}else{
					svo.on += MeterStatusManager.monitorMeterStatusInterval ;
					svo.onTd += MeterStatusManager.monitorMeterStatusInterval ;
				}
			}else{
				//当前不在线
				svo.off += MeterStatusManager.monitorMeterStatusInterval ;
				svo.offTd += MeterStatusManager.monitorMeterStatusInterval ;
			}
		}
	}

}
