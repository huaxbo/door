package com.am.cs12.config;

public class CoreServerVO {
	
	public Integer remote_maxThreadNum;// 处理测控器数据线程池最大工作线程数, 取值范围1-100

	public Integer remote_minThreadNum;// 处理测控器数据线程池最小工作线程数, 取值范围1-100

	/*
	 * 处理测控器数据线程池的工作线程，当其空闲时长为配置值时， 线程池将回收线程，
	 * 以使线程池保持最小线程数 单位为毫秒(1秒=1000毫秒)
	 * 限制不能小于1000
	 */
	public Integer remote_freeTimeout;

	/*
	 * 处理测控器数据线程池的工作线程，当其不间断工作时长为配置值时， 系统将认为
	 * 线程已经了崩溃，将强制清除 单位为毫秒(1秒=1000毫秒)
	 * 限制不能小于1000
	 */
	public Integer remote_busyTimeout;

	////////////////////////////////////////
	
	public Integer cachCom_maxThreadNum;// 处理缓存命令线程池最大工作线程数, 取值范围1-100

	public Integer cachCom_minThreadNum;// 处理缓存命令线程池最小工作线程数, 取值范围1-100

	/*
	 * 处理缓存命令线程池的工作线程，当其空闲时长为配置值时， 线程池将回收线程，
	 * 以使线程池保持最小线程数 单位为毫秒(1秒=1000毫秒)
	 * 限制不能小于1000
	 */
	public Integer cachCom_freeTimeout;

	/*
	 * 处理缓存命令线程池的工作线程，当其不间断工作时长为配置值时， 系统将认为
	 * 线程已经了崩溃，将强制清除 单位为毫秒(1秒=1000毫秒)
	 * 限制不能小于1000
	 */
	public Integer cachCom_busyTimeout;


}
