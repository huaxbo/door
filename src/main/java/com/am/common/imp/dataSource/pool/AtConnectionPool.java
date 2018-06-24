package com.am.common.imp.dataSource.pool;

import java.sql.*;
import java.util.*;
//import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 此类定义了一个连接池.它能够根据要求创建新连接,直到预定的最
 * 大连接数为止.在返回连接给客户程序之前,它能够验证连接的有效性.
 */
class AtConnectionPool {
	private int checkedOut;

	private Vector<MyConnection> freeConnections = new Vector<MyConnection>();

	private int maxConn;

	private String name;

	private String password;

	private String URL;

	private String user;

	private boolean autoCommit;

	private boolean needTestConnection;

	private int timeConWaitNeedTest;

	public boolean newConnectionSucc = true;

	public int serialCreateNewConnectionCount = 0;

	private Logger log = LogManager.getLogger(AtConnectionPool.class.getName());

	/**
	 * 创建新的连接池
	 *
	 * @param name 连接池名字
	 * @param URL 数据库的JDBC URL
	 * @param user 数据库帐号,或 null
	 * @param password 密码,或 null
	 * @param maxConn 此连接池允许建立的最大连接数
	 */
	public AtConnectionPool(String name, String URL, String user,
			String password, int maxConn, boolean autoCommit,
			boolean needTestConnection, int timeConWaitNeedTest) {
		this.name = name;
		this.URL = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
		this.autoCommit = autoCommit;
		this.needTestConnection = needTestConnection;
		this.timeConWaitNeedTest = timeConWaitNeedTest;

	}

	/**
	 * 将不再使用的连接返回给连接池
	 *
	 * @param con 客户程序释放的连接
	 */
	public synchronized void freeConnection(Connection con) {
		// 将指定连接加入到向量末尾
		if (con != null) {
			MyConnection myCon = new MyConnection(con, this.needTestConnection,
					this.timeConWaitNeedTest);
			freeConnections.addElement(myCon);
			checkedOut--;
			log("连接池'" + name + "'回收一个连接");
			notifyAll();
		} else {
			log("连接池'" + name + "'未回收一个连接");
		}
	}

	/**
	 * 从连接池获得一个可用连接.如没有空闲的连接且当前连接数小于最大连接
	 * 数限制,则创建新连接.如原来登记为可用的连接不再有效,则从向量删除之,
	 * 然后递归调用自己以尝试新的可用连接.
	 */
	public synchronized Connection getConnection() {
		MyConnection myCon = null;
		Connection con = null;
		if (freeConnections.size() > 0) {
			// 目前有空闲的连接
			// 获取向量中第一个可用连接
			myCon = (MyConnection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			con = myCon.getConnection();
			try {
				if (con.isClosed()) {
					log("从连接池'" + name + "'删除一个无效的连接！");
					// 递归调用自己,尝试再次获取可用连接
					con = getConnection();
				} else {
					if (myCon.needTest()) {
						if (!myCon.testConnectionIsValid()) {
							log("从连接池'" + name + "'删除一个无效的连接！");
							// 递归调用自己,尝试再次获取可用连接
							con = getConnection();
						}
					}
				}
			} catch (SQLException e) {
				log("从连接池'" + name + "'删除一个无效的连接！");
				// 递归调用自己,尝试再次获取可用连接
				con = getConnection();
			}
			serialCreateNewConnectionCount = 0;
		} else {
			// 目前没有空闲的连接
			if (maxConn == 0 || checkedOut < maxConn) {
				// maxConn == 0 说明没有最大连接数限制
				// 当前的连接存在的有效连接数没有达最上限
				serialCreateNewConnectionCount++;
				if ((maxConn <= 0 && serialCreateNewConnectionCount > 100)
						|| (maxConn > 0 && serialCreateNewConnectionCount >= maxConn)) {
					return null;
				} else {
					if (newConnectionSucc) {
						con = newConnection();
					} else {
						return null;
					}
				}
			}
		}
		if (con != null) {
			checkedOut++;
		}
		notifyAll();
		if (con != null) {
			log("从连接池'" + name + "'得到一个有效连接！");
		} else {
			log("从连接池'" + name + "'未能得到一个有效连接！");
		}
		return con;
	}

	/**
	 * 从连接池获取可用连接.可以指定客户程序能够等待的最长时间
	 * 参见前一个getConnection()方法.
	 *
	 * @param timeout 以毫秒计的等待时间限制
	 */
	public synchronized Connection getConnection(long timeout) {
		//    long startTime = new Date().getTime();
		long startTime = System.currentTimeMillis();
		Connection con;
		while ((con = getConnection()) == null) {
			try {
				wait(timeout);
				// con = getConnection() ;
				if ((System.currentTimeMillis() - startTime) >= timeout) {
					// wait()返回的原因是超时
					notifyAll();
					return con;
				}
			} catch (InterruptedException e) {
			}
			if ((System.currentTimeMillis() - startTime) >= timeout) {
				// wait()返回的原因是超时
				notifyAll();
				return con;
			}
		}
		notifyAll();
		return con;
	}

	/**
	 * 关闭所有连接
	 */
	@SuppressWarnings("unchecked")
	public synchronized void release() {
		Enumeration allConnections = freeConnections.elements();
		MyConnection myCon = null;
		while (allConnections != null && allConnections.hasMoreElements()) {
			Object o = allConnections.nextElement();
			if (o != null) {
				myCon = (MyConnection) o;
				Connection con = myCon.getConnection();
				if (con != null) {
					try {
						con.close();
						log("连接池'" + name + "'关闭了一个连接！");
					} catch (SQLException e) {
						log(e, "连接池'" + name + "'不能关闭一个连接！");
						return;
					}
				}
			}
		}
		freeConnections.removeAllElements();
	}

	/**
	 * 创建新的连接
	 */
	public Connection newConnection() {
		Connection con = null;
		try {
			if (user == null) {
				con = DriverManager.getConnection(URL);
			} else {
				con = DriverManager.getConnection(URL, user, password);
			}
			con.setAutoCommit(this.autoCommit);
			log("连接池'" + name + "'创建了一个新的连接！");
		} catch (SQLException e) {
			newConnectionSucc = false;
			log(e, "不能创建到'" + URL + "'的数据库连接！");
			return null;
		}
		newConnectionSucc = true;
		return con;
	}

	/**
	 * 将文本信息写入日志文件
	 */
	private void log(String msg) {
		log.info(msg);
	}

	/**
	 * 将文本信息与异常写入日志文件
	 */
	private void log(Throwable e, String msg) {
		log.error(msg + "\n" + e);
	}

	/**
	 * 内部类，管理连接计时
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * <p>Copyright: Copyright (c) 2005</p>
	 * <p>Company: </p>
	 * @author not attributable
	 * @version 1.0
	 */
	class MyConnection {
		private Connection con;

		private long time;

		private boolean needTest;

		private int waitTime;

		/**
		 *
		 * @param con Connection
		 */
		public MyConnection(Connection con, boolean needTest, int waitTime) {
			this.con = con;
			this.needTest = needTest;
			if (this.needTest) {
				//this.time = (new Date()).getTime();
				this.time = System.currentTimeMillis();
			}
			this.waitTime = waitTime;
		}

		/**
		 * 计算连接的空等待时间，以便决定是否要进行测试
		 * @return int
		 */
		public boolean needTest() {
			if (this.needTest) {
				int wait = (int) (System.currentTimeMillis() - this.time);
				if (wait > this.waitTime) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		/**
		 * 测试是否还有创建会话
		 * 这是因为可能外界（例如防火墙把在一定时间没有活动的连接给关掉，但JDBC并没有查觉到）
		 * 把这个连接给关掉了
		 * @param con Connection
		 * @return boolean
		 */
		public boolean testConnectionIsValid() {
			Statement st = null;
			try {
				st = con.createStatement();
			} catch (Exception e) {
				return false;
			}
			try {
				st.close();
			} catch (Exception e) {
			}
			return true;
		}

		/**
		 * 得到连接
		 * @return Connection
		 */
		public Connection getConnection() {
			return con;
		}
	}
}
