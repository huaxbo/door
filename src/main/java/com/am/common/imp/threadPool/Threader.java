package com.am.common.imp.threadPool ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.am.common.threadPool.*;

public class Threader extends Thread {
	/**
	 * 标示线程是活的
	 */
	private boolean living = true ;
	
	/**
	 * 线程忙碌或空闲记时
	 */
	protected long time ;
	
	/**
	 * 线程名称，推荐以工作类型命名
	 */
	private String threadName;
	
	/**
	 * 当前线程所处的线程池
	 */
	private ThreadPoolImp pool ;
	/**
	 * 线程具体工作的回调类
	 */
	private ACThreadJob job ;
	
	/**
	 * 指示线程可以工作
	 */
	private Boolean canJob ;

	protected Threader(String threadName , ThreadPoolImp pool) {
		super();
		this.threadName = threadName ;
		this.pool = pool ;
		this.time = 0 ;
		this.canJob = false ;
	}
	
	/**
	 * 设置线程工作对象
	 * @param job
	 */
	protected void setJob(ACThreadJob job ) throws Exception {
		if(job == null){
			this.job = new ACThreadJob(){ 
				public void stop(){
				}
				public void execute(){
				}
			};
		}
		synchronized (this) {
			this.job = job ;
			this.canJob = true;
			//忙碌记时开始
			this.time = System.currentTimeMillis() ;
			this.notify();
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("finally")
	public void run() {
		while (living) {
			synchronized (this) {
				while (!canJob) {
					try {
						this.wait();
					} catch (Exception e) {
						Logger log = LogManager.getLogger(Threader.class.getName());
						log.error("负责子服务(ID+" + this.threadName + ")的工作线程等待可工作信号时发生等待异常", e);
						this.canJob = false ;
						continue;
					}
				}
				try {
					this.job.execute() ;
				}catch (Exception ee) {
					Logger log = LogManager.getLogger(Threader.class.getName());
					log.error("负责子服务(ID=" + this.threadName + ")的工作线程在执行工作时发生异常，" + ee.getMessage(), ee);
					//ee.printStackTrace() ;
				}finally {
					this.canJob = false ;
					this.job = null ;
					this.free() ;
					continue;
				}
			}// end synchronized(this)
		}// end while(living)
	}
	
	public void free(){
		//使本线程回归空闲线程池
		pool.freeThread(this);
		//空闲开始记时
		this.time = System.currentTimeMillis() ;
		// 没有可做的了
		this.canJob = false;
	}

	/**
	 * Destroys this thread.
	 * 
	 * @todo Implement this java.lang.Thread method
	 */
	public void destroy() {
		this.living = false ;
		this.job = null ; 
	}

}
