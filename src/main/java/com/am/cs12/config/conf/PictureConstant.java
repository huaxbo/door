package com.am.cs12.config.conf ;

public class PictureConstant {
	
	//图像文件存储目录(相对目录)
	public static String picDir = "picDir" ;
	
	//当经过picIdle长时间(秒)未收到图像数据，即认为图像数据已经批量传送完毕
	//可以进行图像数据完整性检查及其他工作。
	public static String picIdle = "picIdle" ;

	
	/*
	 *  图像文件生命周期(分钟)，当文件最后修改时间超过此值，则文件将被删除
	 *  取值范围(10-600分钟)
	 *  
	 */
	public static String picFileLifCycle = "picFileLifCycle" ;
	
	/**
	 * key - 处理图像线程池最大工作线程数
	 */
	public static final String pic_maxThreadNum = "pic_maxThreadNum" ;
	
	/**
	 *key - 处理图像线程池最小工作线程数
	 */
	public static final String pic_minThreadNum = "pic_minThreadNum" ;
	
	/**
	 *key - 处理图像线程池的工作线程，当其空闲时长为配置值时，
	      线程池将回收线程，以使线程池保持最小线程数  
	      单位为毫秒(1秒=1000毫秒)
	      限制不能小于1000
	 */
	public static final String pic_freeTimeout = "pic_freeTimeout" ;
	
	/**
	 *key - 处理图像线程池的工作线程，当其不间断工作时长为配置值时，
	      系统将认为线程已经了崩溃，将强制清除  
	      单位为毫秒(1秒=1000毫秒)
	      限制不能小于1000
	 */
	public static final String pic_busyTimeout = "pic_busyTimeout" ;
	

}
