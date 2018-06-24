package com.am.cs12.commu.core.remoteCommand.forGprsSerial;

import com.am.common.timerTask.ACSchedularJob;
import com.am.cs12.config.*;

public class CachWatcherManagerForGprsSerial  {
	private static final String JOB_GROUPNAME = "cachCommandJobsForGPRSSERIAL" ;
	private static final String JOB_CACHWATCHERNAME = "cachWatcherJobForGPRSSERIAL" ;
	
	
	private static CachWatcherManagerForGprsSerial instance ;
	
	private static boolean startedJob = false ;
	
	/**
	 * 私有构造方法
	 */
	private CachWatcherManagerForGprsSerial(){
	}
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static CachWatcherManagerForGprsSerial instance(){
		if(instance == null){
			instance = new CachWatcherManagerForGprsSerial() ;
		}
		return instance ;
	}
	
	/**
	 * 启动观察者
	 * @throws Exception
	 */
	public void startSchedularJobs() throws Exception{
		ProtocolBaseVO bvo = ConfigCenter.instance().getProtocolBaseVO() ;
		if(bvo != null && bvo.sendCommandInterval > 0){
			if(!startedJob){
				//初始化观察者，以完成定时工作作任务
				//每天在指定时间清空当天的状态数据时刻
				new ACSchedularJob().addSecondlyJob(
						JOB_CACHWATCHERNAME , 
						JOB_GROUPNAME , 
						CachWatcherForGprsSerial.class, 
						null ,
						10000 ,
						bvo.sendCommandInterval,
						-1
						) ;
			}
		}
		startedJob = true ;
	}

}