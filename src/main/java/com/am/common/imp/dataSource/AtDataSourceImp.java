package com.am.common.imp.dataSource;

import com.am.common.dataSource.AtConnection;
import java.sql.Connection;
import com.am.common.imp.dataSource.pool.AtDataSourceManager;
import com.am.common.dataSource.AtDataSource;

public class AtDataSourceImp implements AtDataSource {

	private String connectPoolName;

	/**
	 * 构造方法，传来数据源名称(实质是连接池名称)
	 */
	public AtDataSourceImp(String dataSourceName) {
		this.connectPoolName = dataSourceName;
	}


	/**
	 * 得到数据库连接
	 * @return
	 */
	public AtConnection getAtConnetion(long time) {
		Connection c = AtDataSourceManager
				.getInstance()
				.getConnection(connectPoolName, time);
		if (c == null) {
			return null;
		}
		AtConnectionImp cimp = new AtConnectionImp(
				this.connectPoolName, c);
		return cimp;
	}
	/**
	 * 得到数据库连接
	 * @return
	 */
	public AtConnection getAtConnetion() {
		Connection c = AtDataSourceManager
				.getInstance()
				.getConnection(connectPoolName);
		if (c == null) {
			return null;
		}
		AtConnectionImp cimp = new AtConnectionImp(
				this.connectPoolName, c);
		return cimp;
	}

	/**
	 * 销毁所有数据库连接池
	 */
	public void destroyDataSource() {
		AtDataSourceManager.getInstance().release();
	}

}
