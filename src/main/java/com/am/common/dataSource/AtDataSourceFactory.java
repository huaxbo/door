package com.am.common.dataSource;

import com.am.common.imp.dataSource.*;
import com.am.common.imp.dataSource.pool.*;


public class AtDataSourceFactory {
	private static AtDataSourceManager manager;
	/**
	 * 初始化
	 * @param configPath
	 * @throws Exception
	 */
	public static void init(DbConfigVO[] vos) throws Exception {
		if(manager!= null){
			throw new Exception("数据源工厂不能重复初始化！");
		}
		if(vos == null){
			throw new Exception("数据源初始化参数对象为空！");
		}
		manager = AtDataSourceManager.getInstance(vos);
	}
	/**
	 * 得到数据源
	 * @param dataSourceName String 数据源名称
	 * @return AtDataSource  返回的数据源对象
	 */
	public final static AtDataSource lookupDataSource(String dataSourceName)
			throws Exception {
		if (manager == null) {
			throw new Exception("查找数据源前首先必须初始化！");
		}
		return new AtDataSourceImp(dataSourceName);
	}
}
