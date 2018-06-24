package com.am.common.imp.threadPool;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.am.common.threadPool.*;

/**
 * 
 * @author Administrator
 */
public class ThreadPoolImp implements ACThreadPool {
	
	/**
	 * 线程池名称；
	 */
	private String threadPoolName ;
	
	/**
	 * 内部启动的空闲和忙碌超时线程的线程
	 */
	private Thread monitorThread ;

	/**
	 * 空闲线程池
	 */
	private ArrayList<Threader> freeThreads;

	/**
	 * 工作中的线程池
	 */
	private ArrayList<Threader> busiThreads;

	/**
	 * 线程池最大线程数 , 如果最大线程数小于等于0，则maxThreadNum取1值 。
	 */
	private int maxThreadNum;

	/**
	 * 最小线程数，如果最小线程数大于maxThreadNum，则最小线程数取maxThreadNum值。
	 */
	private int minThreadNum;

	/**
	 * 当前线程池中的线程数。
	 */
	private int currThreadNum;
	
	/**
	 * 空闲线程超时的时长(秒)，超过这个时间，就清除多余线程，
	 * 使空闲线程最多是minThreadNum个
	 */
	private long freeTimeout ;
	/**
	 * 忙碌线程超时的时长(秒)，超过这个时间，就认为是崩溃线程，将被清除。
	 */
	private long busyTimeout ;

	/**
	 * 同步对象
	 */
	private Object synObj = new Object();

	
	private Logger log = LogManager.getLogger(MonitorThread.class.getName());

	/**
	 * 线程池构造方法
	 * @param threadPoolName 线程池和线程名称
	 * @param maxThreadNum 线程池最大线程数
	 * @param minThreadNum 线程池最小线程数，或初始线程数
	 * @param freeTimeout 空闲线程超时时长(秒)
	 * @param busyTimeout 忙碌线程超时时长(秒)
	 */
	public ThreadPoolImp(
			String threadPoolName , 
			int maxThreadNum , 
			int minThreadNum ,
			long freeTimeout ,
			long busyTimeout) {
		if(threadPoolName == null){
			threadPoolName="AC工作线程" ;
		}
		this.threadPoolName = threadPoolName ;
		
		if(maxThreadNum <= 0){
			maxThreadNum = 1 ;
		}
		if(minThreadNum > maxThreadNum){
			minThreadNum = maxThreadNum ;
		}
		this.maxThreadNum = maxThreadNum ;
		this.minThreadNum = minThreadNum ;
		currThreadNum = 0;
		
		this.freeTimeout = freeTimeout * 1000 ;
		this.busyTimeout = busyTimeout * 1000 ;

		busiThreads = new ArrayList<Threader>();
		freeThreads = new ArrayList<Threader>();

		//最小化线程池
		for (int i = 0; i < this.minThreadNum ; i++) {
			Threader t = new Threader(this.threadPoolName , this);
			t.start();
			freeThreads.add(t);
			currThreadNum++;
		}
		
		this.monitorThread = new MonitorThread(this) ;
		this.monitorThread.start() ;
	}

	/**
	 * 把所要执行的工作对象实例放入线程池中
	 * @param job ThreadJob 工作对象实例
	 * @throws Exception 
	 */
	public void SetThreadJob(ACThreadJob job) throws Exception {

		synchronized (synObj) {
			log.info("新工作到达服务(ID=" + this.threadPoolName + ")的线程池中。") ;
			Threader t = null ;
			if (freeThreads.size() == 0) {
				//当前没有空闲线程
				if (currThreadNum < maxThreadNum) {
					//当前线程数未达到最大值 , 增加新的线程
					t = new Threader(threadPoolName , this);
					t.start();
					currThreadNum++;
				} else {
					//当前线程达到最大数了，等待释放回来的线程
					while (freeThreads.size() == 0) {
						//如果没有空闲的线程,等待工作线程工作完成回来
						try {
							log.warn("'" + this.threadPoolName + "'线程池中线程数达到上限，新工作任务等待释放线程!"); 
							synObj.wait();
						} catch (Exception e) {
							log.error("'" + this.threadPoolName + "'线程池中线程等待释放线时发生等待异常!", e);
						}
						t = (Threader) freeThreads.get(0);
						if (t != null) {
							//说明得到了释放回来的线程
							freeThreads.remove(0);
							break;
						} else {
							//说明没有得到释放回来的线程，可以其他线程
							continue;
						}
					}//end while
				}//end else
			}//end if(freeThreads.size() == 0)
			else {
				t = (Threader) freeThreads.get(0);
				freeThreads.remove(0);
			}
			busiThreads.add(t);
			t.setJob(job);
		}
	}

	/**
	 * 线程工作完成，从busiThreads回归freeThreads
	 */
	protected void freeThread(Threader t) {
		synchronized (synObj) {
			busiThreads.remove(t);
			freeThreads.add(t);
			synObj.notify();
		}
	}
	
	/**
	 * 监控超时线程的线程
	 * @author Administrator
	 *
	 */
	@SuppressWarnings("unchecked")
	protected class MonitorThread extends Thread {
		private ThreadPoolImp pool ;
		
		private Threader t  ;
		private Iterator it  ;

		/**
		 * 
		 * @param pool
		 */
		public MonitorThread(ThreadPoolImp pool){
			this.pool = pool ;
		}
		/**
		 * 
		 */
		@SuppressWarnings("finally")
		public void run(){
			long time = pool.freeTimeout ;
			if(pool.busyTimeout < pool.freeTimeout){
				time = pool.busyTimeout ;
			}
			boolean isException = false ;
			while(true){
				t = null ;
				it = null ;
				try{
					MonitorThread.sleep(time) ;
				}catch(Exception e){
					isException = true ;
				}finally{
					if(isException){
						isException = false ;
						continue ;
					}
				}
				try{
					synchronized (pool.synObj) {
						if(pool.freeThreads.size() > pool.minThreadNum){
							//如果空闲线程大于最小线程数，则清理空闲线程
							int num = pool.freeThreads.size() - pool.minThreadNum ;
							int count = 0 ;
							it = pool.freeThreads.iterator() ;
							while(it.hasNext()){
								if(count == num) {
									break ;
								}
								count ++ ;
								t = (Threader)it.next() ;
								if((System.currentTimeMillis() - t.time) >= pool.freeTimeout){
									it.remove() ;
									pool.currThreadNum-- ;
									log.info("'" + pool.threadPoolName + "'线程池中清除了一个超时的空闲线程!"); 
									t.destroy() ;
									t = null ;
								}
							}
						}//end if
						
						it = pool.busiThreads.iterator() ;
						while(it.hasNext()){
							t = (Threader)it.next() ;
							if((System.currentTimeMillis() - t.time) >= pool.busyTimeout){
								it.remove() ;
								pool.currThreadNum-- ;
								log.warn("'" + pool.threadPoolName + "'线程池中清除了一个超时的崩溃(忙碌)线程!"); 
								t.destroy() ;
								t = null ;
							}
						}
							
					}//end synchronized (pool.synObj)
				}catch(Exception e){
				}finally{
					continue ;
				}
			}//end while(true)
		}
	}
	
	
}
