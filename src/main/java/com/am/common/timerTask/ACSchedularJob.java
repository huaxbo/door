package com.am.common.timerTask;

import com.am.common.imp.timerTask.QuartzSchedular;
import java.util.*;

public class ACSchedularJob {
	/**
	 * 添加简单的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param delayStart 延迟开始工作时长(单位毫秒)
	 * @param repeatInterval 重复工作间隔时长(单位毫秒)
	 * @param repeatCount  重复工作次数(如果小于0，将一直重复执行下去 , 如果是0执行一次，如果是1执行2次，依次类推)
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addSimpleJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			long repeatInterval ,
			int repeatCount) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addSimpleJob(jobName, jobGroupName, jboClass , jobDataMap , delayStart, repeatInterval, repeatCount) ;
	}
	
	/**
	 * 添加每X秒钟重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param delayStart 延迟开始工作时长(单位毫秒)
	 * @param intervalInSeconds 重复工作间隔时长(秒)
	 * @param repeatCount  重复工作次数(如果小于0，将一直重复执行下去 , 如果是0执行一次，如果是1执行2次，依次类推)
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addSecondlyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			int intervalInSeconds ,
			int repeatCount) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addSecondlyJob(jobName, jobGroupName, jboClass , jobDataMap, delayStart, intervalInSeconds, repeatCount) ;
	}
	/**
	 * 添加每X分钟重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param delayStart 延迟开始工作时长(单位毫秒)
	 * @param intervalInMinute 重复工作间隔时长(分钟)
	 * @param repeatCount  重复工作次数(如果小于0，将一直重复执行下去 , 如果是0执行一次，如果是1执行2次，依次类推)
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addMinutelyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			int intervalInMinute ,
			int repeatCount) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addMinutelyJob(jobName, jobGroupName, jboClass , jobDataMap, delayStart, intervalInMinute, repeatCount) ;
	} 
	/**
	 * 添加每X小时重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param delayStart 延迟开始工作时长(单位毫秒)
	 * @param intervalInHour 重复工作间隔时长(小时)
	 * @param repeatCount  重复工作次数(如果小于0，将一直重复执行下去 , 如果是0执行一次，如果是1执行2次，依次类推)
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addHourlyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			int intervalInHour ,
			int repeatCount) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addHourlyJob(jobName, jobGroupName, jboClass , jobDataMap, delayStart, intervalInHour, repeatCount);
	}
	
	/**
	 * 添加每天某时某分重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param hour 某时(0-23之间(包括0与23))
	 * @param minute 某分(0-59之间(包括0与59))
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addDailyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addDailyJob(jobName, jobGroupName, jboClass , jobDataMap, hour, minute);
	}
	
	/**
	 * 添加每一周的某一天某时某分重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param dayOfWeek 一周的某一天(1代表周一,依次类推)(取值1-7(包括1与7))
	 * @param hour 某时(0-23之间(包括0与23))
	 * @param minute 某分(0-59之间(包括0与59))
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addWeeklyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int dayOfWeek,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addWeeklyJob(jobName, jobGroupName, jboClass , jobDataMap, dayOfWeek, hour, minute) ;
	}
	/**
	 * 添加每月的某一天某时某分重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param dayOfMonth 一月的某一天(1-31之间(包括1与31))
	 * @param hour 某时(0-23之间(包括0与23))
	 * @param minute 某分(0-59之间(包括0与59))
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addMonthlyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int dayOfMonth,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addMonthlyJob(jobName, jobGroupName, jboClass , jobDataMap, dayOfMonth, hour, minute);
	}
	/**
	 * 添加每月的最后一天某时某分重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param hour 某时(0-23之间(包括0与23))
	 * @param minute 某分(0-59之间(包括0与59))
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addLastDateOfMonthJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addLastDateOfMonthJob(jobName, jobGroupName, jboClass , jobDataMap, hour, minute);
	}
	/**
	 * 添加每周一、周二、周三、周四、周五各重复工作一次的工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param jboClass 工作类
	 * @param hour 某时(0-23之间(包括0与23))
	 * @param minute 某分(0-59之间(包括0与59))
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addWorkingDayInWeekJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.start() ;
		QuartzSchedular.addWorkingDayInWeekJob(jobName, jobGroupName, jboClass , jobDataMap, hour, minute) ;
	}

}
