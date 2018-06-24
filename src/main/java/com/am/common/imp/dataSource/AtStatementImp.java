package com.am.common.imp.dataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.common.dataSource.AtStatement ;

/**
 * <p>Title: AT User Manager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: AT</p>
 * @author lry
 * @version 1.0
 */
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class AtStatementImp implements AtStatement {

  private Statement statement ;

  /**
   *
   * @param statement
   */
  public AtStatementImp(Statement statement) {
    this.statement = statement ;
  }

  /**
   *
   */
  public ResultSet executeQuery(String sql) throws SQLException  {
     return this.statement.executeQuery(sql) ;
  }

  /**
   *
   */
  public int executeUpdate(String sql)  throws SQLException{
    return this.statement.executeUpdate(sql) ;
  }
  /**
   *
   */
  public boolean execute(String sql) throws SQLException {
    return this.statement.execute(sql) ;
  }
  /**
   *
   */
  public void close() {
    try{
      if(this.statement != null){
        this.statement.close();
        this.statement = null ;
        Logger log = LogManager.getLogger(AtStatementImp.class.getName()) ;
        log.info("正确关闭了一个数据库会话.");
      }
    }catch(Exception e){
      try{
        if(this.statement != null){
          this.statement.close();
        }
      }catch(Exception ee){
        Logger log = LogManager.getLogger(AtStatementImp.class.getName()) ;
        log.info("出错，不能关闭一个数据库会话.");
      }
    }
    return  ;
  }


}
