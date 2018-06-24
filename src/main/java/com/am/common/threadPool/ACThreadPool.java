package com.am.common.threadPool;


public interface ACThreadPool {
	/**
	 * 把所要执行的工作对象实例放入线程池中
	 * @param job ThreadJob 工作对象实例
	 * @throws Exception 
	 */
	public void SetThreadJob(ACThreadJob job) throws Exception  ;

}
