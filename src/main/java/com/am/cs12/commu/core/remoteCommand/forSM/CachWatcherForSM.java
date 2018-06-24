package com.am.cs12.commu.core.remoteCommand.forSM;

import java.util.*;
//import org.apache.log4j.*;

import com.am.common.timerTask.ACTaskJob;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.config.*;

public class CachWatcherForSM  extends ACTaskJob {


//	private static final Logger log = LogManager.getLogger(CachWatcherForSM.class.getName()) ;
	
	/**
	 * 监视命令缓存
	 */
	@Override
	public void execute() throws Exception {
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		Set<String> rtuIds = meters.keySet() ;
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		CommandQueueForSM queue = null ;
		CommandNodeForSM node = null ;
		long timeout = cc.getProtocolBaseVO().commandTimeout * 1000 ;
		long now = System.currentTimeMillis() ;
		int size = 0 ;
//		log.info("定时处理缓存SM短信通道命令工作任务开始..." ) ;
		for(String rtuId : rtuIds){
			queue = rsm.getCachCommandQueueForSM(rtuId) ;
			size = queue.size() ;
			if(size > 0){
				synchronized(queue){
					for(int i = 0 ; i < size ; i++){
						node = queue.get(i) ;
						if(node != null){
							if(now - node.sendTime > timeout){
								queue.remove(node) ;
								size-- ;
								i-- ;
							}
						}
					}
				}
			}
		}
//		log.info("定时处理缓存SM短信通道命令工作任务结束。" ) ;
	}

}
