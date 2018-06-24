package com.am.common.imp.dataSource.pool;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.common.dataSource.* ;


/**
 * 管理类DBConnectionManager支持对一个或多个由属性文件定义的数据库连接
 * 池的访问.客户程序可以调用getInstance()方法访问本类的唯一实例.
 */
public class AtDataSourceManager {

	static private Logger log = LogManager.getLogger(AtDataSourceManager.class.getName());

	static private AtDataSourceManager instance; // 唯一实例
	/**
	 * 当前正在用连接池中连接的客户数量
	 */
	static private int clients;
	/**
	 * 多个连接池的数据库连接的驱动
	 */
	static private Vector<Driver> drivers = new Vector<Driver>(); 
	/**
	 * 对应不同数据源的各个连接池
	 */
	static private Hashtable<String, AtConnectionPool> pools = new Hashtable<String, AtConnectionPool>(); 
	/**
	 * 标识各个数据源对映的各个连接池是否都创建成功，并且能提供有效连接
	 */
	static private Hashtable<String, Boolean> poolsConnectSucc = new Hashtable<String, Boolean>(); 
	/**
	 * 监视个数据源对映的各个连接池的线程，如果一个连接池不能创建世功，那么这个线程将一直尝试创建它
	 */
	static private Hashtable<String, ReCreatePoolThread> reCreatePoolsThreads = new Hashtable<String, ReCreatePoolThread>(); //重新创建连接池的线程。
	/**
	 * 监视个数据源对映的各个连接池的线程所用的同步对象
	 */
	static private Hashtable<String, Object> poolsCreateThreadSynObj = new Hashtable<String, Object>(); //存放标识连接池是有效连接的标志。

	/**
	 * 返回唯一实例.如果是第一次调用此方法,则创建实例
	 * 只在服务器初始化时调用
	 * @return DBConnectionManager 唯一实例
	 */
	static synchronized public AtDataSourceManager getInstance(DbConfigVO[] vos) {
		if (instance == null) {
			instance = new AtDataSourceManager();
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
	static synchronized public AtDataSourceManager getInstance() {
		return instance;
	}

	/**
	 * 建构函数私有以防止其它对象创建本类实例
	 */
	private AtDataSourceManager() {
	}

	/**
	 * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数
	 * 限制,则创建并返回新连接
	 *
	 * @param name 在属性文件中定义的连接池名字
	 * @return Connection 可用连接或null
	 */
	public Connection getConnection(String poolName) {
		if (poolsConnectSucc.get(poolName).booleanValue()) {
			AtConnectionPool pool = (AtConnectionPool) pools.get(poolName);
			if (pool != null) {
				Connection con = pool.getConnection();
				if (con == null) {
					if (!pool.newConnectionSucc) {
						poolsConnectSucc.put(poolName, new Boolean(false));

						Object o = AtDataSourceManager.poolsCreateThreadSynObj
								.get(poolName);
						synchronized (o) {
							o.notifyAll();
						}

					}
					return null;
				} else {
					clients++;
					return con;
				}
			}
		}
		return null;
	}

	/**
	 * 获得一个可用连接.若没有可用连接,且已有连接数小于最大连接数限制,
	 * 则创建并返回新连接.否则,在指定的时间内等待其它线程释放连接.
	 *
	 * @param name 连接池名字
	 * @param time 以毫秒计的等待时间
	 * @return Connection 可用连接或null
	 */
	public Connection getConnection(String poolName, long time) {
		if (poolsConnectSucc.get(poolName).booleanValue()) {
			AtConnectionPool pool = (AtConnectionPool) pools.get(poolName);
			if (pool != null) {
				Connection con = pool.getConnection(time);
				if (con == null) {
					if (!pool.newConnectionSucc) {
						log.error("从数据源" + poolName + "中产生新连接时失败!");
						poolsConnectSucc.put(poolName, new Boolean(false));
						Object o = AtDataSourceManager.poolsCreateThreadSynObj
								.get(poolName);
						synchronized (o) {
							o.notifyAll();
						}

					}
					return null;
				} else {
					clients++;
					return con;
				}
			}
		}
		return null;
	}

	/**
	 * 将连接对象返回给由名字指定的连接池
	 *
	 * @param name 连接池名字
	 * @param con 连接对象
	 */
	public void freeConnection(String poolName, Connection con) {
		AtConnectionPool pool = (AtConnectionPool) pools.get(poolName);
		if (pool != null) {
			if (con != null) {
				clients--;
			}
			pool.freeConnection(con);
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
			AtConnectionPool pool = (AtConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				log.info("撤销驱动程序" + driver.getClass().getName() + " ");
			} catch (SQLException e) {
				log.error("不能撤销驱动程序" + driver.getClass().getName(), e);
			}
		}
		drivers.removeAllElements();
	}

	/**
	 * 读取属性完成初始化
	 */
	private boolean init(DbConfigVO[] vo) {
		if (vo == null || vo.length == 0) {
			return false;
		}
		boolean flag = true;
		for (int i = 0; i < vo.length; i++) {
			flag = loadDrivers(vo[i]);
			if (!flag) {
				//驱动没有注册成功
				return false;
			}
		}
		for (int i = 0; i < vo.length; i++) {
			createPools(vo[i]);
		}
		return true;
	}

	/**
	 * 装载和注册所有JDBC驱动程序
	 *
	 * @param props 属性
	 */
	private boolean loadDrivers(DbConfigVO vo) {
		String driverClassName = vo.driverClass.trim();
		try {
			Driver driver = (Driver) Class.forName(driverClassName)
					.newInstance();
			DriverManager.registerDriver(driver);
			drivers.addElement(driver);
			log.info("成功注册了JDBC驱动" + driverClassName);
			return true;
		} catch (Exception e) {
			log.error("错误，不能注册JDBC驱动" + driverClassName + " ! ", e);
			System.out.println("错误，不能注册JDBC驱动" + driverClassName + " ! ");
			return false;
		}
	}

	/**
	 * 根据指定属性创建连接池实例.
	 *
	 * @param props 连接池属性
	 */
	private void createPools(DbConfigVO vo) {
		String dataSourceName = vo.dataSourceName;
		String url = vo.url;
		String user = vo.user;
		String password = vo.password;
		int maxCount = vo.maxCount;
		int minCount = vo.minCount;
		boolean autoCommit = vo.autoCommit;
		boolean needTestConnection = vo.needTestConnection;
		int timeConNeedTest = vo.timeConNeedTest;

		AtConnectionPool pool = new AtConnectionPool(dataSourceName, url,
				user, password, maxCount, autoCommit, needTestConnection,
				timeConNeedTest);

		pools.put(dataSourceName, pool);
		poolsCreateThreadSynObj.put(dataSourceName, new Object());
		ReCreatePoolThread th = new ReCreatePoolThread(dataSourceName);
		th.start();
		reCreatePoolsThreads.put(dataSourceName, th);

		Connection[] cs = new Connection[minCount];
		for (int i = 0; i < minCount; i++) {
			cs[i] = pool.getConnection();
		}
		for (int i = 0; i < minCount; i++) {
			if (cs[i] != null) {
				pool.freeConnection(cs[i]);
			}
		}
		if (!pool.newConnectionSucc) {
			System.out.println("出错，不能产生到数据库的连接，请检查数据源配置、网络以及数据库服务器！");
			poolsConnectSucc.put(dataSourceName, new Boolean(false));

			Object o = AtDataSourceManager.poolsCreateThreadSynObj
					.get(dataSourceName);
			synchronized (o) {
				o.notifyAll();
			}

		} else {
			poolsConnectSucc.put(dataSourceName, new Boolean(true));
			log.info("成功创建数据库数据源'" + dataSourceName + "'");
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
				Object o = AtDataSourceManager.poolsCreateThreadSynObj
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
				while (!AtDataSourceManager.poolsConnectSucc
						.get(this.poolName).booleanValue()) {
					//连接池新连接不能创建
					count++;
					AtConnectionPool pool = AtDataSourceManager.pools
							.get(this.poolName);
					Connection cs = pool.newConnection();
					if (cs != null) {
						pool.freeConnection(cs);
					}
					if (!pool.newConnectionSucc) {
						AtDataSourceManager.poolsConnectSucc.put(
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
						AtDataSourceManager.poolsConnectSucc.put(
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
