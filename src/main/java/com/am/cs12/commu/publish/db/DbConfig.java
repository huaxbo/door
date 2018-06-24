package com.am.cs12.commu.publish.db;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.am.cs12.util.*;
import com.am.cs12.config.*;
import com.am.util.NumberUtil;

public class DbConfig  extends Config{
	
    private DbConfigVO vo ;
	
	public DbConfig(){
		vo = new DbConfigVO() ;
	}


	/**
	 * 分析服务器运行参数
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DbConfigVO parseOptions(String filePath) throws IllegalArgumentException {
		if(this.doc == null){
			super.createDom(filePath) ;
		}
		if(this.doc == null){
			throw new IllegalArgumentException(filePath + "语法有错误，不能创建" + filePath + "文件的doc对象!");
		}
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到" + filePath + "文件的根元素!");
		}
		
		
		List list = root.getChildren();
		Iterator it = list.iterator();
		Element e = null;
		String name = null ;
		String value = null ;
		int tempInt = 0 ;
		while (it.hasNext()) {
			e = (Element) it.next();
			name = e.getName() ;
			value = e.getText() ;
			if(value != null){
				value = value.trim() ;
			}else{
				throw new IllegalArgumentException(filePath + "中元素" +name + "数值为空！");
			}
			
			if (name.equals(DbConstant.getConnectTimeout_key)) {
				if (!NumberUtil.isIntNumber(value)) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + DbConstant.getConnectTimeout_key + "配置数值必须是整数！");
				}
				tempInt = Integer.parseInt(value) ;
				if(tempInt < 0){
					throw new IllegalArgumentException(filePath
							+ "中元素" + DbConstant.getConnectTimeout_key + "配置数值必须大于等于0间取值！");
				}
				vo.getConnectTimeout = tempInt ;
			}else if (name.equals(DbConstant.dbSourceName_key)) {
				if (value == null || value.equals("")) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + DbConstant.dbSourceName_key + "必须配置！");
				}
				if(!this.isExistDataSource(value)){
					throw new IllegalArgumentException(filePath
							+ "中元素" + DbConstant.dbSourceName_key + "配置的数据源名" + value + "在数据源配置中不存在！");
				}
				vo.dbSourceName = value ;
			}else if (name.equals(DbConstant.table_key)) {
				if (value == null || value.equals("")) {
					throw new IllegalArgumentException(filePath
							+ "中元素" + DbConstant.table_key + "必须配置！");
				}
				vo.table = value ;
			}
		}
		
		return vo ;
	}
	
	/**
	 * 检查数据源是否存在
	 * @param filePath
	 * @param dataSourceName
	 * @return
	 */
	private boolean isExistDataSource(String dataSourceName) {
		com.am.common.dataSource.DbConfigVO[] dbs = ConfigCenter.instance().getDbConfigVO() ;
		if(dbs != null){
			for(int i = 0 ; i < dbs.length ; i++){
				if(dbs[i].dataSourceName.equals(dataSourceName)){
					return true ;
				}
			}
		}
		return false ;
	}
}

