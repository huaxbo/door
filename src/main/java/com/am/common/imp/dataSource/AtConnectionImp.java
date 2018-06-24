package com.am.common.imp.dataSource;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.common.imp.dataSource.pool.AtDataSourceManager;
import com.am.common.dataSource.AtConnection;
import com.am.common.dataSource.AtStatement;

//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;

public class AtConnectionImp implements AtConnection {

	private Connection connection;
	private String connectPoolName;
	/**
	 *
	 * @param connectPoolName
	 * @param connection
	 */
	public AtConnectionImp(
			String connectPoolName, 
			Connection connection) {
		this.connectPoolName = connectPoolName;
		this.connection = connection;

	}

	/**
	 *创建数据库会话
	 * @return
	 */
	public AtStatement getAtStatement() {
		try {
			Statement st = this.connection.createStatement();
			AtStatementImp at = new AtStatementImp(st);
			return at;
		} catch (Exception e) {
			Logger log = LogManager.getLogger(AtConnectionImp.class.getName());
			log.error("创建数据库会话出错:\n", e);
			return null;
		}
	}

	//  /**
	//   * 生成调用存储过程的会话
	//   * @param process String
	//   * @return CallableStatement
	//   */
	//  public CallableStatement prepareCall(String process) {
	//    try{
	//      return this.connection.prepareCall(process);
	//    }catch(Exception e){
	//      Logger log  = LogManager.getLogger(AtConnectionImp.class.getName()) ;
	//      log.error("创建数据库调用存储过程的会话出错:\n" , e);
	//      return null ;
	//    }
	//
	//
	//  }
	//
	//  public PreparedStatement preparedStatement(String sql) {
	//    try{
	//      return     this.connection.prepareStatement(sql) ;
	//    }catch(Exception e){
	//      Logger log  = LogManager.getLogger(AtConnectionImp.class.getName()) ;
	//      log.error("创建数据库可设参数的会话出错:\n" , e);
	//      return null ;
	//    }
	//  }

	/**
	 * 是否关闭了连接
	 */
	public boolean isClosed() {
		try {
			return this.connection.isClosed();
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 *
	 */
	public void commit() {
		try {
			this.connection.commit();
		} catch (Exception e) {
			Logger log = LogManager.getLogger(AtConnectionImp.class.getName());
			log.error("提交数据库事务出错!\n", e);
		}
	}

	/**
	 *
	 * @param flag
	 */
	public void setAutoCommit(boolean flag) {
		try {
			this.connection.setAutoCommit(flag);
		} catch (Exception e) {
			Logger log = LogManager.getLogger(AtConnectionImp.class.getName());
			log.error("设置数据库事务提交类型出错!\n", e);
		}

	}

	/**
	 *
	 */
	public void rollback() {
		try {
			this.connection.rollback();
		} catch (Exception e) {
			Logger log = LogManager.getLogger(AtConnectionImp.class.getName());
			log.error("回滚数据库事务出错!\n", e);
		}

	}

	/**
	 *
	 */
	public void free() {
		AtDataSourceManager.getInstance()
		.freeConnection(
				this.connectPoolName,
				this.connection);
		this.connection = null;
	}

	/**
	 * 因为这个连接状态上看上有效的，但可能是无效了，例如当远程数据库关闭又重启动了
	 * 那么这个连接实际上是无效的，它不能建立Statement了。所以有必要设置此方法关闭
	 * 这个无效的连接。
	 */
	public void close() {
		try {
			this.connection.close();
		} catch (Exception e) {
			this.connection = null;
		}
	}

}
