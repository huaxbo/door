package com.am.common.imp.timerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.* ;
import org.quartz.impl.*;
import java.util.*;

public class QuartzSchedular {
	
	private static QuartzSchedular instance  ;
	private static Scheduler scheduler ;
	
	private QuartzSchedular(){
	}
	/**
	 *  启动容器
	 * @return
	 */
	public static void start(){
		if(instance == null){
			instance = new QuartzSchedular() ;
			try{
				SchedulerFactory factory = new StdSchedulerFactory() ;
				scheduler = factory.getScheduler() ;
				scheduler.start() ;
			}catch(SchedulerException e){
				Logger log = LogManager.getLogger(QuartzSchedular.class.getName()) ;
				log.error("初始化quartz的Scheduler发生异常!" , e) ;
			}
		}
		return  ;
	}
	
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
	public static void addSimpleJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			long repeatInterval ,
			int repeatCount) throws Exception {
		QuartzSchedular.checkDelayStart(delayStart) ;
		if(repeatInterval < 1000){
			throw new Exception("重复工作间隔时长(单位毫秒)不能小于1000毫秒(即1秒)!") ;
		}
		repeatCount = QuartzSchedular.checkRepeatCount(repeatCount) ;

		JobDetail jobDetail = new JobDetail(jobName , jobGroupName , jboClass);
		if(jobDataMap != null){
			JobDataMap map = jobDetail.getJobDataMap();
			Set<Map.Entry<String , Object>> set = jobDataMap.entrySet() ;
			Iterator<Map.Entry<String , Object>> it = set.iterator() ;
			Map.Entry<String , Object> entry = null ;
			String key = null ;
			Object value = null ;
			while(it.hasNext()){
				entry = it.next() ;
				key = entry.getKey() ;
				value = entry.getValue() ;
				map.put(key, value);
			}
		}
		
		SimpleTrigger trigger = new SimpleTrigger(jobName + "trigger" , jobGroupName) ;
		trigger.setRepeatInterval(repeatInterval) ;
		trigger.setRepeatCount(repeatCount) ;
		trigger.setStartTime(new Date(System.currentTimeMillis() + delayStart)) ;
		
		try{
			scheduler.scheduleJob(jobDetail , trigger) ;
		}catch(Exception e){
			throw new Exception(e) ;
		}
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
	public static void addSecondlyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			int intervalInSeconds ,
			int repeatCount) throws Exception {
		QuartzSchedular.checkDelayStart(delayStart) ;
		if(intervalInSeconds < 1){
			throw new Exception("重复工作间隔时长(秒)不能小于1秒)!") ;
		}
		repeatCount = QuartzSchedular.checkRepeatCount(repeatCount) ;
		
		Trigger trigger = TriggerUtils.makeSecondlyTrigger(jobName + "trigger" , intervalInSeconds , repeatCount) ;
		QuartzSchedular.addJob(jobName, jobGroupName, jboClass , jobDataMap , delayStart , trigger) ;
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
	public static void addMinutelyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			int intervalInMinute ,
			int repeatCount) throws Exception {
		QuartzSchedular.checkDelayStart(delayStart) ;
		if(intervalInMinute < 1){
			throw new Exception("重复工作间隔时长(单位分钟)不能小于1分钟)!") ;
		}
		repeatCount = QuartzSchedular.checkRepeatCount(repeatCount) ;
		
		Trigger trigger = TriggerUtils.makeMinutelyTrigger(jobName + "trigger" , intervalInMinute , repeatCount) ;
		QuartzSchedular.addJob(jobName, jobGroupName, jboClass , jobDataMap , delayStart , trigger) ;
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
	public static void addHourlyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			int intervalInHour ,
			int repeatCount) throws Exception {
		QuartzSchedular.checkDelayStart(delayStart) ;
		if(intervalInHour < 1){
			throw new Exception("重复工作间隔时长(单位小时)不能小于1小时)!") ;
		}
		repeatCount = QuartzSchedular.checkRepeatCount(repeatCount) ;
		
		Trigger trigger = TriggerUtils.makeHourlyTrigger(jobName + "trigger" , intervalInHour , repeatCount) ;
		QuartzSchedular.addJob(jobName, jobGroupName, jboClass , jobDataMap , delayStart , trigger) ;
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
	public static void addDailyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.checkHour(hour) ;
		QuartzSchedular.checkMinute(minute) ;
		
		Trigger trigger = TriggerUtils.makeDailyTrigger(jobName + "trigger" , hour , minute) ;
		QuartzSchedular.addJob2(jobName, jobGroupName, jboClass , jobDataMap , trigger) ;
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
	public static void addWeeklyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int dayOfWeek,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.checkHour(hour) ;
		QuartzSchedular.checkMinute(minute) ;
		dayOfWeek = QuartzSchedular.checkDayOfWeek(dayOfWeek) ;
		
		Trigger trigger = TriggerUtils.makeWeeklyTrigger(jobName + "trigger" , dayOfWeek , hour , minute) ;
		QuartzSchedular.addJob2(jobName, jobGroupName, jboClass , jobDataMap , trigger) ;
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
	public static void addMonthlyJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int dayOfMonth,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.checkHour(hour) ;
		QuartzSchedular.checkMinute(minute) ;
		QuartzSchedular.checkDayOfMonth(dayOfMonth) ;
		
		Trigger trigger = TriggerUtils.makeMonthlyTrigger(jobName + "trigger" , dayOfMonth , hour , minute) ;
		QuartzSchedular.addJob2(jobName, jobGroupName, jboClass  , jobDataMap , trigger) ;
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
	public static void addLastDateOfMonthJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.checkHour(hour) ;
		QuartzSchedular.checkMinute(minute) ;
		CronTrigger ct = new CronTrigger(jobName + "trigger" , jobGroupName) ;
		try{
			ct.setCronExpression(new CronExpression("0 " + minute + " " + hour + " L * ? *" )) ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e) ;
		}
		
		QuartzSchedular.addJob3(jobName, jobGroupName, jboClass , jobDataMap , ct) ;
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
	public static void addWorkingDayInWeekJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			int hour ,
			int minute) throws Exception {
		QuartzSchedular.checkHour(hour) ;
		QuartzSchedular.checkMinute(minute) ;
		CronTrigger ct = new CronTrigger(jobName + "trigger" , jobGroupName) ;
		try{
			ct.setCronExpression(new CronExpression("0 " + minute + " " + hour + " ? * " + TriggerUtils.MONDAY + "-" +TriggerUtils.FRIDAY + " *" )) ;
		}catch(Exception e){
			throw new Exception(e.getMessage() , e) ;
		}
		
		QuartzSchedular.addJob3(jobName, jobGroupName, jboClass , jobDataMap , ct) ;
	}
	
	/**
	 * 添加工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param clazz 工作类
	 * @param delayStart 延迟开始工作时长(单位毫秒)
	 * @param trigger  触发器
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private static void addJob(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			long delayStart,
			Trigger trigger) throws Exception {
		trigger.setGroup(jobGroupName) ;
		trigger.setStartTime(new Date(System.currentTimeMillis() + delayStart)) ;
		JobDetail jobDetail = new JobDetail(jobName , jobGroupName , jboClass);
		if(jobDataMap != null){
			JobDataMap map = jobDetail.getJobDataMap();
			Set<Map.Entry<String , Object>> set = jobDataMap.entrySet() ;
			Iterator<Map.Entry<String , Object>> it = set.iterator() ;
			Map.Entry<String , Object> entry = null ;
			String key = null ;
			Object value = null ;
			while(it.hasNext()){
				entry = it.next() ;
				key = entry.getKey() ;
				value = entry.getValue() ;
				map.put(key, value);
			}
		}
		try{
			scheduler.scheduleJob(jobDetail , trigger) ;
		}catch(Exception e){
			throw new Exception(e) ;
		}
	}
	/**
	 * 添加工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param clazz 工作类
	 * @param trigger  触发器
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private static void addJob2(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			Trigger trigger) throws Exception {
		trigger.setGroup(jobGroupName) ;
		JobDetail jobDetail = new JobDetail(jobName , jobGroupName , jboClass);
		if(jobDataMap != null){
			JobDataMap map = jobDetail.getJobDataMap();
			Set<Map.Entry<String , Object>> set = jobDataMap.entrySet() ;
			Iterator<Map.Entry<String , Object>> it = set.iterator() ;
			Map.Entry<String , Object> entry = null ;
			String key = null ;
			Object value = null ;
			while(it.hasNext()){
				entry = it.next() ;
				key = entry.getKey() ;
				value = entry.getValue() ;
				map.put(key, value);
			}
		}
		try{
			scheduler.scheduleJob(jobDetail , trigger) ;
		}catch(Exception e){
			throw new Exception(e) ;
		}
	}
	/**
	 * 添加工作任务，
	 * @param jobName 工作名称
	 * @param jobGroupName 工作组名称
	 * @param clazz 工作类
	 * @param trigger  触发器
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private static void addJob3(
			String jobName, 
			String jobGroupName, 
			Class jboClass , 
			HashMap<String , Object> jobDataMap ,
			CronTrigger trigger) throws Exception {
		JobDetail jobDetail = new JobDetail(jobName , jobGroupName , jboClass);
		if(jobDataMap != null){
			JobDataMap map = jobDetail.getJobDataMap();
			Set<Map.Entry<String , Object>> set = jobDataMap.entrySet() ;
			Iterator<Map.Entry<String , Object>> it = set.iterator() ;
			Map.Entry<String , Object> entry = null ;
			String key = null ;
			Object value = null ;
			while(it.hasNext()){
				entry = it.next() ;
				key = entry.getKey() ;
				value = entry.getValue() ;
				map.put(key, value);
			}
		}
		//Scheduler scheduler = QuartzSchedular.getScheduler();
		try{
			scheduler.scheduleJob(jobDetail , trigger) ;
		}catch(Exception e){
			throw new Exception(e) ;
		}
	}
	
	/**
	 * 
	 * @param delayStart
	 */
	private static void checkDelayStart(long delayStart)throws Exception {
		if(delayStart < 0){
			throw new Exception("延迟开始工作时长(单位毫秒)不能小于0!") ;
		}
	}
	/**
	 * 
	 * @param repeatCount
	 * @return
	 */
	private static int checkRepeatCount(int repeatCount){
		if(repeatCount <= -1){
			repeatCount = SimpleTrigger.REPEAT_INDEFINITELY ;
		}
		return repeatCount ;
	}
	
	/**
	 * 
	 * @param hour
	 * @throws Exception
	 */
	private static void checkHour(int hour)throws Exception {
		if(hour < 0 || hour >= 24){
			throw new Exception("重复工作的小时时间点取值必须在0-23之间(包括0与23)!") ;
		}
	}
	/**
	 * 
	 * @param minute
	 * @throws Exception
	 */
	private static void checkMinute(int minute)throws Exception {
		if(minute < 0 || minute >= 60){
			throw new Exception("重复工作的分钟时间点取值必须在0-59之间(包括0与59)!") ;
		}
	}
	/**
	 * 
	 * @param dayOfWeek
	 * @throws Exception
	 */
	private static int checkDayOfWeek(int dayOfWeek)throws Exception {
		if(dayOfWeek < 1 || dayOfWeek > 7){
			throw new Exception("重复工作的一星期的某一天取值必须在1-7间(包括1与7)!") ;
		}
		int d = 0 ;
		if(dayOfWeek == 1){
			d = TriggerUtils.MONDAY ;
		}else if(dayOfWeek == 2){
			d = TriggerUtils.TUESDAY ;
		}else if(dayOfWeek == 3){
			d = TriggerUtils.WEDNESDAY ;
		}else if(dayOfWeek == 4){
			d = TriggerUtils.THURSDAY ;
		}else if(dayOfWeek == 5){
			d = TriggerUtils.FRIDAY ;
		}else if(dayOfWeek == 6){
			d = TriggerUtils.SATURDAY ;
		}else if(dayOfWeek == 7){
			d = TriggerUtils.SUNDAY ;
		}
		return d ;
	}
	/**
	 * 
	 * @param dayOfMonth
	 * @throws Exception
	 */
	private static void checkDayOfMonth(int dayOfMonth)throws Exception {
		if(dayOfMonth < 1 || dayOfMonth > 31){
			throw new Exception("重复工作的分钟时间点取值必须在1-31之间(包括1与31)!") ;
		}
	}
	
}
