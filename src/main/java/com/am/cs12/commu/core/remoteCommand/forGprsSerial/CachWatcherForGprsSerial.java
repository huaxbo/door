package com.am.cs12.commu.core.remoteCommand.forGprsSerial;

//import org.apache.log4j.*;
import java.util.Map;
import java.util.Set;
import com.am.common.timerTask.ACTaskJob;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.config.*;

public class CachWatcherForGprsSerial  extends ACTaskJob {


//	private static final Logger log = LogManager.getLogger(CachWatcherForGprs.class.getName()) ;
	
	/**
	 * 定时任务，
	 * 监视命令缓存
	 * 以发送命令
	 */
	@Override
	public void execute() throws Exception {
		Map<String, MeterVO> meters = ConfigCenter.instance().getId_meterMap() ;
		Set<String> rtuIds = meters.keySet() ;
		
		DealCachCommandForGprsSerail cc = DealCachCommandForGprsSerail.instance() ;
//		log.info("定时处理缓存GPRS网络通道命令工作任务开始..." ) ;
		for(String rtuId : rtuIds){
			CommandQueueForGprsSerial queue = RemoteSessionManager.instance().getCachCommandQueueForGprsSerial(rtuId) ;
			if(queue != null && queue.sizeOfCanDeal() > 0) {
				cc.timerTaskStartSendCommand(rtuId) ;
			}
		}
//		log.info("定时处理缓存GPRS网络通道命令工作任务结束。" ) ;
	}

}
