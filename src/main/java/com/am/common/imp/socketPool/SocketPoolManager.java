package com.am.common.imp.socketPool;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.am.common.socketPool.*;

/**
 * 管理类支持对一个或多个由属性文件定义的TCP连接
 * 池的访问.客户程序可以调用getInstance()方法访问本类的唯一实例.
 */
public class SocketPoolManager {

	static private Logger log = LogManager.getLogger(SocketPoolManager.class.getName());

	static private SocketPoolManager instance; // 唯一实例
	/**
	 * 当前正在用连接池中连接的客户数量
	 */
	static private int clients;
	/**
	 * 对应不同TCP的各个连接池
	 */
	static private Hashtable<String, SocketPool> pools = new Hashtable<String, SocketPool>(); 
	/**
	 * 标识各个TCP对映的各个连接池是否都创建成功，并且能提供有效连接
	 */
	static private Hashtable<String, Boolean> poolSucc = new Hashtable<String, Boolean>(); 
	/**
	 * 监视各个TCP对映的各个连接池的线程，如果一个连接池不能创建成功，那么这个线程将一直尝试创建它
	 */
	static private Hashtable<String, ReCreatePoolThread> reCreatePoolsThreads = new Hashtable<String, ReCreatePoolThread>(); //重新创建连接池的线程。
	/**
	 * 监视各个TCP对映的各个连接池的线程所用的同步对象
	 */
	static private Hashtable<String, Object> poolsCreateThreadSynObj = new Hashtable<String, Object>(); //存放标识连接池是有效连接的标志。

	/**
	 * 返回唯一实例.如果是第一次调用此方法,则创建实例
	 * 只在服务器初始化时调用
	 * @return DBConnectionManager 唯一实例
	 */
	static synchronized public SocketPoolManager getInstance(SocketConfigVO[] vos) {
		if (instance == null) {
			instance = new SocketPoolManager();
			//初始化
			if (!instance.init(vos)) {
				instance = null;
			}
		}
		return instance;
	}

	/**
	 * 返回唯一实例.如果是第一次调用此方法,则创建实例
	 * 在服务器启动后，具体应用中调用。
	 * @return DBConnectionManager 唯一实例
	 */
	static synchronized public SocketPoolManager getInstance() {
		return instance;
	}

	/**
	 * 建构函数私有以防止其它对象创建本类实例
	 */
	private SocketPoolManager() {
	}

	/**
	 * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数
	 * 限制,则创建并返回新连接
	 *
	 * @param name 在属性文件中定义的连接池名字
	 * @return Connection 可用连接或null
	 */
	public PoolSocket getSocket(String poolName) {
		if (poolSucc.get(poolName).booleanValue()) {
			SocketPool pool = (SocketPool) pools.get(poolName);
			if (pool != null) {
				PoolSocket soc = pool.getSocket();
				if (soc == null) {
					if (!pool.newSocketSucc) {
						poolSucc.put(poolName, new Boolean(false));

						Object o = SocketPoolManager.poolsCreateThreadSynObj
								.get(poolName);
						synchronized (o) {
							o.notifyAll();
						}

					}
					return null;
				} else {
					clients++;
					return soc;
				}
			}
		}
		return null;
	}

	/**
	 * 获得一个可用连接.若没有可用连接,且已有连接数小于最大连接数限制,
	 * 则创建并返回新连接.否则,在指定的时间内等待其它线程释放连接.
	 *
	 * @param poolName 连接池名字
	 * @param time 以毫秒计的等待时间
	 * @return Socket 可用连接或null
	 */
	public PoolSocket getSocket(String poolName, long time) {
		if (poolSucc.get(poolName).booleanValue()) {
			SocketPool pool = (SocketPool) pools.get(poolName);
			if (pool != null) {
				PoolSocket soc = pool.getSocket(time);
				if (soc == null) {
					if (!pool.newSocketSucc) {
						log.error("从TCP" + poolName + "中产生新连接时失败!");
						poolSucc.put(poolName, new Boolean(false));
						Object o = SocketPoolManager.poolsCreateThreadSynObj
								.get(poolName);
						synchronized (o) {
							o.notifyAll();
						}

					}
					return null;
				} else {
					clients++;
					return soc;
				}
			}
		}
		return null;
	}

	/**
	 * 将连接对象返回给由名字指定的连接池
	 *
	 * @param name 连接池名字
	 * @param soc 连接对象
	 */
	public void freeSocket(String poolName, PoolSocket soc) {
		SocketPool pool = (SocketPool) pools.get(poolName);
		if (pool != null) {
			if (soc != null) {
				clients--;
			}
			pool.freeSocket(soc);
		}
	}
	/**
	 * 消毁一个无效连接
	 * @param poolName 池名
	 * @param soc socket
	 */
	public void destroySocket(String poolName , PoolSocket soc){
		SocketPool pool = (SocketPool) pools.get(poolName);
		if (pool != null) {
			if (soc != null) {
				clients--;
			}
			pool.destroySocket(soc);
		}
	}
	/**
	 * 关闭所有连接,撤销驱动程序的注册
	 */
	@SuppressWarnings("unchecked")
	public synchronized void release() {
		// 等待直到最后一个客户程序调用
		int count = 0;
		while (clients > 0) {
			count++;
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				;
			}
			if (count == 9) {
				break;
			}
		}

		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			SocketPool pool = (SocketPool) allPools.nextElement();
			pool.release();
		}
	}

	/**
	 * 读取属性完成初始化
	 */
	private boolean init(SocketConfigVO[] vo) {
		if (vo == null || vo.length == 0) {
			return false;
		}
		for (int i = 0; i < vo.length; i++) {
			createPools(vo[i]);
		}
		return true;
	}


	/**
	 * 根据指定属性创建连接池实例.
	 *
	 * @param props 连接池属性
	 */
	private void createPools(SocketConfigVO vo) {
		String poolName = vo.poolName;
		String url = vo.url;
		int port = vo.port;
		int maxCount = vo.maxCount;
		int minCount = vo.minCount;
		int maxCreatingNum = vo.maxCreatingNum ;

		SocketPool pool = new SocketPool(poolName, url,
				port, maxCount , maxCreatingNum);

		pools.put(poolName, pool);
		poolsCreateThreadSynObj.put(poolName, new Object());
		ReCreatePoolThread th = new ReCreatePoolThread(poolName);
		th.start();
		reCreatePoolsThreads.put(poolName, th);

		PoolSocket[] cs = new PoolSocket[minCount];
		for (int i = 0; i < minCount; i++) {
			cs[i] = pool.getSocket();
		}
		for (int i = 0; i < minCount; i++) {
			if (cs[i] != null) {
				pool.freeSocket(cs[i]);
			}
		}
		if (!pool.newSocketSucc) {
			System.out.println("出错，不能产生到" + vo.url + ":" + vo.port + "的TCP的连接，请检查TCP配置、网络以及TCP服务器！");
			poolSucc.put(poolName, new Boolean(false));

			Object o = SocketPoolManager.poolsCreateThreadSynObj
					.get(poolName);
			synchronized (o) {
				o.notifyAll();
			}

		} else {
			poolSucc.put(poolName, new Boolean(true));
			log.info("成功创建到" + vo.url + ":" +  vo.port + "的TCP的连接! " + poolName);
		}
	}

	/**
	 * 当连接池创建不成功或者是连接当前不能连接了
	 * 启动这个线程，进行尝试连接
	 * @author Administrator
	 */
	private class ReCreatePoolThread extends Thread {

		private String poolName;

		private int count = 0;

		public ReCreatePoolThread(String poolName) {
			this.poolName = poolName;
		}

		public void run() {
			while (true) {
				Object o = SocketPoolManager.poolsCreateThreadSynObj
						.get(this.poolName);
				synchronized (o) {
					try {
						o.wait();
					} catch (Exception e) {
						try {
							ReCreatePoolThread.sleep(60000);
						} catch (Exception ee) {
							;
						}
					}
				}
				while (!SocketPoolManager.poolSucc
						.get(this.poolName).booleanValue()) {
					//连接池新连接不能创建
					count++;
					SocketPool pool = SocketPoolManager.pools
							.get(this.poolName);
					PoolSocket cs = pool.newSocket();
					if (cs != null) {
						pool.freeSocket(cs);
					}
					if (!pool.newSocketSucc) {
						SocketPoolManager.poolSucc.put(
								this.poolName, new Boolean(false));
						try {
							if (count < 10) {
								ReCreatePoolThread.sleep(1000);
							} else {
								ReCreatePoolThread.sleep(60000);
								count = 0;
							}
						} catch (Exception e) {
							try {
								if (count < 100) {
									ReCreatePoolThread.sleep(1000);
								} else {
									ReCreatePoolThread.sleep(60000);
									count = 0;
								}
							} catch (Exception ee) {
								;
							}
						}
					} else {
						SocketPoolManager.poolSucc.put(
								this.poolName, new Boolean(true));
						synchronized (o) {
							try {
								o.wait();
							} catch (Exception e) {
								try {
									ReCreatePoolThread.sleep(60000);
								} catch (Exception ee) {
									;
								}
							}
						}
					}
				}
			}
		}
	}

}
