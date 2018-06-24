package com.am.cs12.commu.dataSource ;

import com.am.common.dataSource.* ;
import com.am.cs12.config.ConfigCenter;


public class DataSourceServer {
	
	/**
	 * 服务启动
	 * @throws Exception 
	 */
	public void start(String serverId) throws Exception {
	
		DbConfigVO[] vos = ConfigCenter.instance().getDbConfigVO() ;
		AtDataSourceFactory.init(vos) ;
		System.out.println("数据库连接池服务(ID:" + serverId + ")已经启动");
	}


}
