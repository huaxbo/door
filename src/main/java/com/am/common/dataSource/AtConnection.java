package com.am.common.dataSource;

//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;

/**
 * <p>Title: AT User Manager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: AT</p>
 * @author lry
 * @version 1.0
 */


public interface AtConnection  {

  public AtStatement getAtStatement();
//  public CallableStatement prepareCall(String process) ;
//  public PreparedStatement preparedStatement(String sql) ;
  /**
   * 向数据库提交数据
   */
  public void commit();
  /**
   * 设置自动提交或手动提交方式，为true时自动提交，为false时为手动提交
   * @param flag
   */
  public void setAutoCommit(boolean flag) ;
  /**
   * 回滚
   *
   */
  public void rollback();
  /**
   * 释放到连接池
   *
   */
  public void free() ;
  /**
   * 判断是否连接已经关闭
   * @return
   */
  public boolean isClosed() ;
  /**
   * 因为这个连接状态上看上有效的，但可能是无效了，例如当远程数据库关闭又重启动了
   * 那么这个连接实际上是无效的，它不能建立Statement了。所以有必要设置此方法关闭这个无效的连接。
   */
  public void close() ;
}
