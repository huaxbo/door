package com.am.cs12.config;

public class PictureVO {

	//图像文件存储目录(相对目录) -->
	public String picDir ;
	/*
	 * 当经过picIdle长时间(秒)未收到图像数据，即认为图像数据已经批量传送完毕
     * 可以进行图像数据完整性检查及其他工作。
     * 取值范围(2-30秒)
	 */
	public Long picIdle ;
	
	/*
	 *  图像文件生命周期(分钟)，当文件最后修改时间超过此值，则文件将被删除
	 *  取值范围(10-600分钟)
	 *  
	 */
	public Long picFileLifCycle ;
	
	
	
	public Integer pic_maxThreadNum;// 处理测图像线程池最大工作线程数, 取值范围1-100

	public Integer pic_minThreadNum;// 处理测图像线程池最小工作线程数, 取值范围1-100

	/*
	 * 处理测图像线程池的工作线程，当其空闲时长为配置值时， 线程池将回收线程，
	 * 以使线程池保持最小线程数 单位为毫秒(1秒=1000毫秒)
	 * 限制不能小于1000
	 */
	public Integer pic_freeTimeout;

	/*
	 * 处理测图像线程池的工作线程，当其不间断工作时长为配置值时， 系统将认为
	 * 线程已经了崩溃，将强制清除 单位为毫秒(1秒=1000毫秒)
	 * 限制不能小于1000
	 */
	public Integer pic_busyTimeout;


	
	public PictureVO(){
		this.picDir = "picDir/" ;
		this.picIdle = 10 * 1000L ;
		this.picFileLifCycle = 30 * 60 * 1000L ;
	}

}
