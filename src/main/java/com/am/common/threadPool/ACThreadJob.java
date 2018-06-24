package com.am.common.threadPool;

public interface ACThreadJob {
	/**
	 * 线程池工作类的回调方法。
	 * 注意：
	 * 1、这个方法内，推荐不使用try catch语句，如果确实需要用，
	 * 要一定用try catch finnaly语句，而且finnaly内不要
	 * 再有可能产生异常。
	 * 2、这个方法内不能执行永循环工作，例如有while(true)这样
	 * 的工作任务，否则至超时时间，系统强制停止此工作。
	 * 3、这个方法内只能执行短时间即成完成的工作，工作完成后即退出本方法体。
	 * @throws Exception
	 */
	public void execute() throws Exception ;
	
	/**
	 * 停止线程对此项式作的执行
	 * @throws Exception
	 */
	public void stop() throws Exception ;

}
