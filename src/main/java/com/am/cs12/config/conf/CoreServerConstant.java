package com.am.cs12.config.conf ;

public class CoreServerConstant {
	/**
	 * key - 处理测控器数据线程池最大工作线程数
	 */
	public static final String remote_maxThreadNum = "remote_maxThreadNum" ;
	
	/**
	 *key - 处理测控器数据线程池最小工作线程数
	 */
	public static final String remote_minThreadNum = "remote_minThreadNum" ;
	
	/**
	 *key - 处理测控器数据线程池的工作线程，当其空闲时长为配置值时，
	      线程池将回收线程，以使线程池保持最小线程数  
	      单位为毫秒(1秒=1000毫秒)
	      限制不能小于1000
	 */
	public static final String remote_freeTimeout = "remote_freeTimeout" ;
	
	/**
	 *key - 处理测控器数据线程池的工作线程，当其不间断工作时长为配置值时，
	      系统将认为线程已经了崩溃，将强制清除  
	      单位为毫秒(1秒=1000毫秒)
	      限制不能小于1000
	 */
	public static final String remote_busyTimeout = "remote_busyTimeout" ;
	
/////////////////////////////////////////
	/**
	 * key - 处理缓存命令线程池最大工作线程数
	 */
	public static final String cachCom_maxThreadNum = "cachCom_maxThreadNum" ;
	
	/**
	 *key - 处理缓存命令线程池最小工作线程数
	 */
	public static final String cachCom_minThreadNum = "cachCom_minThreadNum" ;
	
	/**
	 *key - 处理缓存命令线程池的工作线程，当其空闲时长为配置值时，
	      线程池将回收线程，以使线程池保持最小线程数  
	      单位为毫秒(1秒=1000毫秒)
	      限制不能小于1000
	 */
	public static final String cachCom_freeTimeout = "cachCom_freeTimeout" ;
	
	/**
	 *key - 处理缓存命令线程池的工作线程，当其不间断工作时长为配置值时，
	      系统将认为线程已经了崩溃，将强制清除  
	      单位为毫秒(1秒=1000毫秒)
	      限制不能小于1000
	 */
	public static final String cachCom_busyTimeout = "cachCom_busyTimeout" ;
	
	


}
