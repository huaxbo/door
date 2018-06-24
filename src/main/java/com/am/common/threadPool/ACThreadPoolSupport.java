package com.am.common.threadPool;

import com.am.common.imp.threadPool.*;

public class ACThreadPoolSupport {

	/**
	 * 得到一个线程池实例
	 * @param threadPoolName 线程池和线程名称
	 * @param maxThreadNum 线程池最大线程数
	 * @param minThreadNum 线程池最小线程数，或初始线程数
	 * @param freeTimeout 空闲线程超时时长(秒)
	 * @param busyTimeout 忙碌线程超时时长(秒)
	 * @return 线程池实例
	 */
	public ACThreadPool newThreadPool(String threadPoolName, int maxThreadNum,
			int minThreadNum, long freeTimeout, long busyTimeout) {
		return new ThreadPoolImp(threadPoolName, maxThreadNum, minThreadNum,
				freeTimeout, busyTimeout);
	}

	/**
	 * 把所要执行的工作对象实例放入线程池中
	 * @param pool 线程池对象实例
	 * @param job 执行的工作对象实例
	 * @throws Exception
	 */
	public void SetThreadJob(ACThreadPool pool, ACThreadJob job)
			throws Exception {
		pool.SetThreadJob(job);
	}

}
