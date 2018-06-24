package com.am.common.dataSource;

/**
 * <p>Title: AT User Manager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: AT</p>
 * @author lry
 * @version 1.0
 */

public class DbConfigVO {

  public String dataSourceName ;
  public String driverClass ;
  public String url ;
  public String user ;
  public String password ;
  public int maxCount ;
  public int minCount ;
  public boolean autoCommit ;
  public boolean needTestConnection ;
  public int timeConNeedTest ;
 

  public DbConfigVO() {
  }

}
