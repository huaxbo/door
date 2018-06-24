package com.am.cs12.config.conf ;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import com.am.common.dataSource.* ;
import com.am.cs12.util.*;

public class DataSourceConfig  extends Config{
	
    private DbConfigVO dbConfigVO[] = null;
	
	public DataSourceConfig(){
	}


	/**
	 * 分析服务器运行参数
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DbConfigVO[] parseOptions(String filePath)  throws IllegalArgumentException {
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}

	    List dataSourceList = root.getChildren();
	    if (dataSourceList == null || dataSourceList.size() == 0) {
	      throw new IllegalArgumentException(filePath + "中不存在dataSources配置!" , null) ;
	    }

	    int num = dataSourceList.size();

	    dbConfigVO = new DbConfigVO[num];
	    num = 0;

	    Iterator it = dataSourceList.iterator();
	    String temp = null;
	    Element tempe = null ;
	    int tempInt = 0;
	    while (it.hasNext()) {
	      Element e = (Element) it.next();
	      tempe = e.getChild(DataSourceConstant.XMLDATASOURCENAME) ;
	      if(tempe == null){throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLDATASOURCENAME + "配置不正确!" , null) ;}
	      dbConfigVO[num] = new DbConfigVO();
	      dbConfigVO[num].dataSourceName = tempe.getText();
	      tempe = e.getChild(DataSourceConstant.XMLDRIVERCLASS) ;
	      if(tempe == null){throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLDRIVERCLASS + "配置不正确!" , null) ;}
	      dbConfigVO[num].driverClass = tempe.getText();
	      tempe = e.getChild(DataSourceConstant.XMLURL) ;
	      if(tempe == null){throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLURL + "配置不正确!" , null) ;}
	      dbConfigVO[num].url = tempe.getText();
	      
	      tempe = e.getChild(DataSourceConstant.XMLUSER) ;
	      if(tempe == null){throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLUSER + "配置不正确!" , null) ;}
	      dbConfigVO[num].user = tempe.getText();
	      
	      tempe = e.getChild(DataSourceConstant.XMLPASSWORD) ;
	      if(tempe == null){throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLPASSWORD + "配置不正确!" , null) ;}
	      dbConfigVO[num].password = tempe.getText();
	      tempe = e.getChild(DataSourceConstant.XMLNEEDTESTCONNECTION) ;
	      if(tempe == null){throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLNEEDTESTCONNECTION + "配置不正确!" , null) ;}
	      if(!(tempe.getText().equals("true")) && !(tempe.getText().equals("false"))){
	        throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLNEEDTESTCONNECTION + "配置不正确!" , null) ;
	      }
	      dbConfigVO[num].needTestConnection = (tempe.getText()).equals("true")?true:false;
	      int timeConNeedTest = 0 ;
	      try{
	        timeConNeedTest = Integer.parseInt(e.getChild(DataSourceConstant.XMLTIMECONNEEDTEST).
	                                           getText().trim());
	      }catch(IllegalArgumentException eint){
	        throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLTIMECONNEEDTEST + "配置不是正确正整数!" , eint)  ;
	      }

	      dbConfigVO[num].timeConNeedTest = timeConNeedTest * 60 * 1000;//分钟变成毫秒

	      temp = e.getChild(DataSourceConstant.XMLMAXCOUNT).getText();
	      try {
	        tempInt = Integer.parseInt(temp.trim());
	      }
	      catch (IllegalArgumentException ex1) {
	        throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLMAXCOUNT + "配置不正确!" , ex1)  ;
	      }
	      dbConfigVO[num].maxCount = tempInt;

	      temp = e.getChild(DataSourceConstant.XMLMINCOUNT).getText();
	      try {
	        tempInt = Integer.parseInt(temp.trim());
	      }
	      catch (IllegalArgumentException ex1) {
	        throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLMINCOUNT + "配置不正确!" , ex1)  ;
	      }
	      dbConfigVO[num].minCount = tempInt;

	      temp = e.getChild(DataSourceConstant.XMLAUTOCOMMIT).getText();
	      if(!(tempe.getText().equals("true")) && !(tempe.getText().equals("false"))){
	        throw new IllegalArgumentException(filePath + "中元素" + DataSourceConstant.XMLAUTOCOMMIT + "配置不正确!" , null) ;
	      }
	      if (temp != null && temp.equals("false")) {
	        dbConfigVO[num].autoCommit = false;
	      }
	      else {
	        dbConfigVO[num].autoCommit = true;
	      }
	      num++;
	    }

	    return dbConfigVO ;
	  }

}
