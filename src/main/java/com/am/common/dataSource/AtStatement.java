package com.am.common.dataSource;

/**
 * <p>Title: AT User Manager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: AT</p>
 * @author lry
 * @version 1.0
 */
import java.sql.ResultSet;
import java.sql.SQLException;


public interface AtStatement  {

  public ResultSet executeQuery(String sql) throws SQLException;
  public int executeUpdate(String sql)  throws SQLException;
  public boolean execute(String sql) throws SQLException;
  public void close() ;
}
