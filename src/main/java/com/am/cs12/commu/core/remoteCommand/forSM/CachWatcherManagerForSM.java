package com.am.cs12.commu.core.remoteCommand.forSM;

import com.am.common.timerTask.ACSchedularJob;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.ProtocolBaseVO;

public class CachWatcherManagerForSM  {
	private static final String JOB_GROUPNAME = "cachCommandJobsForSM" ;
	private static final String JOB_CACHWATCHERNAME = "cachWatcherJobForSM" ;
	
	
	private static CachWatcherManagerForSM instance ;
	
	private static boolean startedJob = false ;
	
	/**
	 * 私有构造方法
	 */
	private CachWatcherManagerForSM(){
	}
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static CachWatcherManagerForSM instance(){
		if(instance == null){
			instance = new CachWatcherManagerForSM() ;
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
						CachWatcherForSM.class, 
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