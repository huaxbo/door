package com.am.cs12.commu.publish.db;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.Data ;
import com.am.cs12.commu.publish.PublishServer;
import com.am.cs12.commu.publish.PublishServerContext;


public class DbServer implements PublishServer{
	/////////////////////
	//不能使用静态成员
	public String thisServerId ;
	private String configPath ;
	private DbConfigVO configVO ;
	private Logger log ;
	
	private Object synObj ;
	private List<Object[]> dataList ;
	private boolean enable ;
	
	private ReceiveDataThread thread ;

	/**
	 * 启动服务
	 */
	@Override
	public PublishServerContext start(String thisServerId, String configPath)
			throws Exception {
		
		this.thisServerId = thisServerId ;
		this.configPath = configPath ;
		log = LogManager.getLogger(this.thisServerId) ;
		
		DbContext cxt = new DbContext() ;
		
		DbConfig conf = new DbConfig() ;
		conf.createDom(this.configPath) ;
		configVO = conf.parseOptions(configPath) ;
		
		cxt.setObject(DbConstant.configVO_key, configVO) ;
		
		enable = true ;
		synObj = new Object() ;
		dataList = new ArrayList<Object[]>() ;
		
		thread = new ReceiveDataThread() ;
		thread.start() ;
		
		System.out.println("数据库数据发布服务(ID:" + this.thisServerId + ")已经启动" ) ;
		return cxt;
	}
	/**
	 * 停止服务
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void stop() {
		if(thread != null){
			thread.destroy() ;
		}
		enable = false ;
	}
	/**
	 * 使能继续处理数据
	 */
	@Override
	public void enable() {
		enable = true ;
	}
	/**
	 * 使不能处理数据
	 */
	@Override
	public void disable(){
		enable = false ;
	}
	/**
	 * 放入数据，以供发布
	 * @param commandId 命令ID，如果是主动上报数据，则其为空
	 * @param data
	 */
	@Override
	public void putData(String commandId , Data d) throws Exception {
		if(enable){
			synchronized (synObj) {
				dataList.add(new Object[]{commandId, d});
				synObj.notifyAll() ;
			}
		}else{
			log.error("该服务(ID+" + this.thisServerId + ")已经被禁止，不能再接收数据") ;
		}
	}
	/**
	 * 放入数据，以供发布
	 * @param commandId 命令ID，如果是主动上报数据，则其为空
	 * @param list
	 */
	@Override
	public void putData(String commandId , List<Data> list)throws Exception{
		if(enable){
			synchronized(synObj) {
				Iterator<Data> it = list.iterator();
				while (it.hasNext()) {
					dataList.add(new Object[]{commandId, it.next()});
				}
				synObj.notifyAll() ;
			}
		}else{
			log.error("该服务(ID+" + this.thisServerId + ")已经被禁止，不能再接收数据") ;
		}
	}

	/**
	 * 取出数据
	 * @return
	 */
	private Object[] popData() {
		synchronized (synObj) {
			if (dataList.size()==0) {
				return null;
			} else {
				return dataList.remove(0);
			}
		}
	}
	/**
	 * 处理数据
	 * @param o
	 */
	private void dealData(Object[] data){
		DbBusi busi = new DbBusi(this.thisServerId , configVO.dbSourceName , configVO.table , configVO.getConnectTimeout) ;
		busi.insertData((String)data[0], (Data)data[1]) ;
	}

	/**
	 * 发现数据线程
	 * @author liu runyu
	 *
	 */
	private class ReceiveDataThread extends Thread {
		public void run() {
			Object[] o = null ;
			while (true) {
				try{
					o = null ;
					if(enable){
						o = popData() ;
					}
					if(o != null){
						dealData(o) ;
					}else{
						try {
							synchronized (synObj) {
								synObj.wait() ;
							}
						} catch (Exception e) {
						}finally{
						}
					}
				} catch (Exception e) {
				}finally{
				}
			}
		}
	}
	
	

}
