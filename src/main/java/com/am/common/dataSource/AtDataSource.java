package com.am.common.dataSource;

/**
 * <p>Title: AT User Manager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: AT</p>
 * @author lry
 * @version 1.0
 */


public interface AtDataSource  {
	  public AtConnection getAtConnetion(long waitTime) ;
	  public AtConnection getAtConnetion() ;
}
