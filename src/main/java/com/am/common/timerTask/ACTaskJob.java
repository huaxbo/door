package com.am.common.timerTask;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.am.common.imp.timerTask.TaskJob;

/**
 * 定时调度工作任务类
 * 这是个抽象类，要继承它，实现execute() throws Exception方法
 * @author Administrator
 *
 */
public abstract class ACTaskJob extends TaskJob {
	
	public void execute(JobExecutionContext ctx) throws JobExecutionException{
		try{
			this.execute() ;
		}catch(Exception e){
			throw new JobExecutionException(e) ;
		}
	}
	
	/**
	 * 定时调度的工作类。
	 * @throws CommuException
	 */
	public abstract void execute() throws Exception ;

}
