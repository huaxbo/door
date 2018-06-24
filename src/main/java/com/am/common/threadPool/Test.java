package com.am.common.threadPool;

public class Test {

	public static void main(String args[]) {
		Test t = new Test();
		t.testt();
	}

	/**
	 * 测试增加线程线程
	 *
	 */
	private void testt() {
		ACThreadPool pool = new ACThreadPoolSupport().newThreadPool("测试线程组",
				10, 5, 5, 5);
		try {
			Thread.sleep(1000);
			int n = 1;
			while (n < 100) {
				System.out.println("加入" + n++ + "工作任务");
				pool.SetThreadJob(new ACThreadJob() {
					public void execute() {
						System.out.println("工作" + this.hashCode());
						try {
							Thread.sleep(2000);
						} catch (Exception e) {
							;
						}
					}
					public void stop(){
						;
					}
				});
				Thread.sleep(100);
			}
		} catch (Exception e) {

		}
	}

}
