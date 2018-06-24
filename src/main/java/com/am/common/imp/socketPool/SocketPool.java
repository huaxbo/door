package com.am.common.imp.socketPool;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


/**
 * 此类定义了一个TPC连接池.它能够根据要求创建新连接,直到预定的最
 * 大连接数为止.在返回连接给客户程序之前,它能够验证连接的有效性.
 */
class SocketPool {
	/**
	 * 池名称a
	 */
	private String name ;
	/**
	 * 当前从连接池取出的连接数
	 */
	private int checkedOut;

	/**
	 * 空闲连接的池
	 */
	private Vector<PoolSocket> freeSockets = new Vector<PoolSocket>();

	/**
	 * 连接数据的最大上限
	 */
	private int maxNum;
	/**
	 * 允许同时创建连接的最大数量
	 */
	private int maxCreatingNum  ;

	/**
	 * 连接的URL
	 */
	private String URL;
	
	/**
	 * 连接的端口号
	 */
	private int PORT ;

	/**
	 * 上次新创建连接是否成功
	 */
	public boolean newSocketSucc = true;
	/**
	 * 正在同时创建socket的数量
	 */
	public int creatingSocketNum = 0 ;
	/**
	 * 连续创建新连接记数
	 */
	public int contineCreateNewSocketCount = 0;
	
	private Logger log = LogManager.getLogger(SocketPool.class.getName());

	/**
	 * 创建新的TPC连接池
	 * @param URL  
	 * @param maxNum 此TPC连接池允许建立的最大连接数, 当maxNum=0时，没有最大连接数据限制
	 */
	public SocketPool(
			String name ,
			String URL,
			int port ,
			int maxNum ,
			int maxCreatingNum 
			) {
		this.name = name ;
		this.URL = URL;
		this.PORT = port ;
		this.maxNum = maxNum;
		this.maxCreatingNum = maxCreatingNum ;
		checkedOut = 0 ;
	}

	/**
	 * 将不再使用的连接返回给TPC连接池
	 *
	 * @param con 客户程序释放的连接
	 */
	public synchronized void freeSocket(PoolSocket soc) {
		// 将指定连接加入到向量末尾
		if (soc != null) {
			soc.newCreate = false ;
			freeSockets.addElement(soc);
			checkedOut--;
			log("TPC连接池" + this.name + "回收一个TCP连接");
			notifyAll();
		} else {
			log("TPC连接池" + this.name + "未回收一个连接");
		}
	}
	/**
	 * 消毁一个无效连接
	 * @param poolName 池名
	 * @param soc socket
	 */
	public void destroySocket(PoolSocket soc){
		this.closeSocket(soc) ;
	}
	/**
	 * 从TPC连接池获得一个可用连接.如没有空闲的连接且当前连接数小于最大连接
	 * 数限制,则创建新连接.如原来登记为可用的连接不再有效,则从向量删除之,
	 * 然后递归调用自己以尝试新的可用连接.
	 */
	public synchronized PoolSocket getSocket() {
		PoolSocket soc = null;
		Socket s = null ;
		if (freeSockets.size() > 0) {
			// 目前有空闲的连接
			// 获取向量中第一个可用连接
			soc = (PoolSocket) freeSockets.firstElement();
			freeSockets.removeElementAt(0);
			s = soc.socket ;
			if (!s.isConnected() || s.isClosed()) {
				this.closeSocket(soc) ;
				log("从TPC连接池" + this.name + "删除一个无效的连接！");
				// 递归调用自己,尝试再次获取可用连接
				soc = getSocket();
			} 
			contineCreateNewSocketCount = 0 ;
		} else {
			// 目前没有空闲的连接
			if (maxNum == 0 || checkedOut < maxNum) {
				// maxNum == 0 说明没有最大连接数限制
				// 当前存在的有效连接数没有达最上限
				if ((maxNum <= 0 && contineCreateNewSocketCount > 100)
						|| (maxNum > 0 && contineCreateNewSocketCount >= maxNum)) {
					return null;
				} else {
					if (newSocketSucc) {
						if(creatingSocketNum < this.maxCreatingNum){
							soc = newSocket();
						}
					} else {
						return null;
					}
				}
			}
		}
		if (soc != null) {
			checkedOut++;
		}
		notifyAll();
		if (soc != null) {
			log("从TPC连接池" + this.name + "得到一个有效连接！");
		} else {
			log("从TPC连接池" + this.name + "未能得到一个有效连接！");
		}
		return soc;
	}

	/**
	 * 从TPC连接池获取可用连接.可以指定客户程序能够等待的最长时间
	 * 参见前一个getSocket()方法.
	 *
	 * @param timeout 以毫秒计的等待时间限制
	 */
	public synchronized PoolSocket getSocket(long timeout) {
		long startTime = System.currentTimeMillis();
		PoolSocket con;
		while ((con = getSocket()) == null) {
			try {
				wait(timeout);
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
		Enumeration allSockets = freeSockets.elements();
		PoolSocket soc = null;
		while (allSockets != null && allSockets.hasMoreElements()) {
			Object o = allSockets.nextElement();
			if (o != null) {
				soc = (PoolSocket) o;
				if (soc != null) {
					this.closeSocket(soc) ;
				}
			}
		}
		freeSockets.removeAllElements();
	}

	/**
	 * 创建新的连接
	 */
	public PoolSocket newSocket() {
		PoolSocket ps = null ;
		Socket s = null;
		InputStream in = null ;
		OutputStream out = null ;
		creatingSocketNum++ ;
		try {
			s = new Socket(InetAddress.getByName(this.URL), this.PORT);
			in = s.getInputStream() ;
			out = s.getOutputStream() ;
			ps = new PoolSocket(s , out , in , true) ;
		} catch (IOException e) {
			newSocketSucc = false;
			creatingSocketNum-- ;
			log(e, "TPC连接池" + this.name + "不能创建到'" + this.URL + ":" +  this.PORT + "'的TCP连接！");
			return null;
		}
		newSocketSucc = true;
		creatingSocketNum-- ;
		return ps ;
	}
	
	/**
	 * 
	 * @param soc
	 */
	private void closeSocket(PoolSocket soc){
		try{
			soc.out.close() ;
			soc.in.close() ;
			soc.socket.close() ;
		}catch(Exception e){
			;
		}
		log("TPC连接池" + this.name + "关闭了一个连接！");
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
	 * 
	 * @author Administrator
	 *
	 */

}
