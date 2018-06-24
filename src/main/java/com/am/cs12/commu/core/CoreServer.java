package com.am.cs12.commu.core;

import com.am.common.threadPool.ACThreadPool;
import com.am.common.threadPool.ACThreadPoolSupport;
import com.am.cs12.config.*;
import com.am.cs12.commu.core.remoteStatus.* ;
import com.am.cs12.commu.core.remoteCommand.forGprsSerial.*;
import com.am.cs12.commu.core.remoteCommand.forSM.CachWatcherManagerForSM;

public class CoreServer  {
	
	public static ACThreadPool remotePool ;//处理接收到的远程测控终端数据的线程池
	public static ACThreadPool cachCommandPool ;//处理缓存命令的线程池
	
	/**
	 * 服务启动
	 * @throws Exception 
	 */
	public void start(String serverId) throws Exception {
		CoreServerVO vo = ConfigCenter.instance().getCoreServerVO() ;
		//得到处理远程测控器数据的线程池的配置
		int maxThreadNum = vo.remote_maxThreadNum ;
		int minThreadNum = vo.remote_minThreadNum ;
		long freeTimeout = vo.remote_freeTimeout ;
		long busyTimeout = vo.remote_busyTimeout ;
		remotePool = new ACThreadPoolSupport().newThreadPool(serverId,
				maxThreadNum, minThreadNum, freeTimeout, busyTimeout) ;
		
		//得到处理缓存命令线程池的配置
		maxThreadNum = vo.cachCom_maxThreadNum ;
		minThreadNum = vo.cachCom_minThreadNum ;
		freeTimeout = vo.cachCom_freeTimeout ;
		busyTimeout = vo.cachCom_busyTimeout ;
		cachCommandPool = new ACThreadPoolSupport().newThreadPool(serverId,
				maxThreadNum, minThreadNum, freeTimeout, busyTimeout) ;
		
		
		///////////////////////////////
		//启动测控终端状态管理器
		MeterStatusManager.instance().startSchedularJobs() ;
		
		///////////////////////////////
		//启动监视GPRS网络通道命令缓存的定时任务，以扫描各个测控终端的命令队列，以发送命令或清除命令
		CachWatcherManagerForGprsSerial.instance().startSchedularJobs() ;
		
		
		///////////////////////////////
		//启动监视SM短信通道命令缓存的定时任务，以扫描各个测控终端的命令队列，以发送命令或清除命令
//		CachWatcherManagerForSM.instance().startSchedularJobs() ;
		
		System.out.println("核心调度服务(ID:" + serverId + ")已经启动");
	}

}