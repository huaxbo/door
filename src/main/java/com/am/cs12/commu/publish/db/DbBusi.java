package com.am.cs12.commu.publish.db;

import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import com.am.common.dataSource.*;
import com.am.cs12.commu.protocol.Data;
import com.am.util.*;

public class DbBusi {
	private String thisServerId ;
	private String dbSourceName ;
	private String table ;
	private int timeout ;
	private AtDataSource ds ;
	private AtConnection con ;
	private AtStatement stm ;
	private String commandId ;
	private Data d ;
	
	private static Logger log = LogManager.getLogger(DbBusi.class.getName()) ;
	
	/**
	 * 初始化方法，只为子服务启动时调度，以备设置socket池的名称
	 * @param socketPoolName
	 */
	public DbBusi(String thisServerId ,
			String dbSourceName ,
			String table ,
			int timeout){
		this.thisServerId = thisServerId ;
		this.dbSourceName = dbSourceName ;
		this.table = table ;
		this.timeout = timeout ;
	}
	/**
	 * 初始化方法，只为子服务启动时调度，以备设置socket池的名称
	 * @param socketPoolName
	 */
	public DbBusi(String thisServerId ,
			String dbSourceName ,
			String table ){
		this.thisServerId = thisServerId ;
		this.dbSourceName = dbSourceName ;
		this.table = table ;
		this.timeout = -1 ;
	}
	
	/**
	 * 输出数据
	 * @param commandId 数据所对应的命令ID
	 * @param xml xml类型数据
	 */
	public void insertData(String commandId , Data d){
    	try{
			if(this.getConnectAndStament()){
				this.commandId = commandId ;
				this.d = d ;
				this.insertData() ;
			}
	    }catch(Exception e ){
			log.error("出错！" + e.getMessage()) ; 
	    }finally{
	    	try{
			    this.endThisSave() ;
	    	}catch(Exception e){}finally{}
	    }
	}
	
	/**
	 * 得到数据连接与会话
	 * @return
	 */
	private boolean getConnectAndStament() throws Exception{
		ds = AtDataSourceFactory.lookupDataSource(dbSourceName) ;
		if(ds == null){
			log.error("出错！本地DB子服务(ID:" + this.thisServerId + ")中，不能得数据源:" + dbSourceName + "！放弃本次数据传输!") ; 
			return false ;
		}
		if(this.timeout == -1 || this.timeout == 0){
			con = ds.getAtConnetion() ;
		}else{
			con = ds.getAtConnetion(timeout) ;
		}
		if(con == null){
			log.error("出错！本地DB子服务(ID:" + this.thisServerId + ")中，不能从数据源:" + dbSourceName + "得到一个有效连接！放弃本次数据传输!") ; ;
			return false ;
		}
		stm = con.getAtStatement() ;
		if(stm == null){
			log.error("出错！本地DB子服务(ID:" + this.thisServerId + ")中，不能从数据源:" + dbSourceName + "有效连接中得到会话！放弃本次数据传输!") ; ;
			return false ;
		}
		return true ;
	}
	
	/**
	 * 把数据插入数据库
	 * @param commandId
	 * @param xml
	 * @param stm
	 * @throws SQLException
	 */
	private void insertData()throws Exception{
		String outData = this.d.toXml() ;
		if (Level.DEBUG.isMoreSpecificThan(log.getLevel())) {
			log.info("数据库服务(ID:" + this.thisServerId + ")存入数据库中的数据:\n" + outData ) ; ;
		}
	
		String id = UniqueId.create() ;
        String stamp = DateTime.yyyyMMddHHmmss() ;

        String sql = "insert into " + table + " " ;
	    sql += " (id , " ;
	    if(commandId != null){
		    sql += " commandId , " ;
	    }
	    sql += " data , stamp) " ;
	    sql += " values(";
	    sql += "'" + id + "' , ";
	    if(commandId != null){
	    	sql += "'" + commandId + "' , ";
	    }
	    sql += "'" + outData + "' , ";
	    sql += "'" + stamp + "' )" ;
    	stm.execute(sql) ;

	}

	/**
	 * 释放Socket连接
	 */
	private void endThisSave(){
	   	try{
			if(this.stm != null){
				this.stm.close() ;
			}
	    }catch(Exception e ){
	    }finally{
			this.stm = null ;
			this.ds = null ;
			if(this.con != null){
				this.con.free() ;
			}
	    }
	}
}

