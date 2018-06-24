package com.am.cs12.commu.core.remoteStatus.watch;

import com.am.common.timerTask.* ;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.util.AmConstant;
import com.am.cs12.commu.core.remoteStatus.operate.StatusXml;


/**
 * 此类是任务调度的工作类，将内存缓存的测控终端状态持久化到xml存储文件中
 * @author Administrator
 *
 */
public class SaveWatcher extends ACTaskJob {

	/**
	 * 将内存缓存的测控终端状态持久化到xml存储文件中
	 */
	@Override
	public void execute() throws Exception {
		new StatusXml().saveMeterStatus(MeterStatusManager.id_status, 
				AmConstant.meterStatusFilePath) ;
	}
}
