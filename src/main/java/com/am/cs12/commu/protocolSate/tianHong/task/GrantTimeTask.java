package com.am.cs12.commu.protocolSate.tianHong.task;


import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.am.common.timerTask.ACTaskJob;
import com.am.cs12.commu.core.remoteSerialPort.TianHongSender;

public class GrantTimeTask  extends ACTaskJob {
	
	private static long lastSendCommandTime = 0l ;

	/**
	 * 每天在指定时间申请卫星授时
	 */
	@Override
	public void execute() throws Exception {
		throw new Exception("该方法未实现")  ;
	}

	public void execute(JobExecutionContext con) throws JobExecutionException {
		JobDataMap data = con.getJobDetail().getJobDataMap() ;
		String code = (String)data.get("code") ;
		String comId = (String)data.get("comId") ;
		String protocolName = (String)data.get("protocolName") ;
		long now = System.currentTimeMillis() ;
		if(Math.abs(now - lastSendCommandTime) > (60*60*1000)){
			TianHongSender.instance().setSelfCommand(comId, code, protocolName) ;
			lastSendCommandTime = now ;
		}
	}

}
