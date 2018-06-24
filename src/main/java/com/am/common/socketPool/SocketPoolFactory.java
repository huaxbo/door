package com.am.common.socketPool;

import java.io.IOException;

import com.am.common.imp.socketPool.*;
import com.am.common.socketPool.SocketPoolFactory;
import com.am.common.imp.socketPool.SocketPoolManager ;

public class SocketPoolFactory {

	private static SocketPoolManager manager ;

	/**
	 * 构造方法
	 */
	private SocketPoolFactory(){}
	
	/**
	 * 得到一个实例
	 * @return
	 */
	public static SocketPoolFactory newInstance(){
		return new SocketPoolFactory() ;
	}
	/**
	 * 初始化
	 * @param configPath
	 * @throws Exception
	 */
	public static void init(SocketConfigVO[] vos) throws Exception {
		if(manager!= null){
			throw new Exception("网络连接池工厂不能重复初始化！");
		}
		if(vos == null){
			throw new Exception("网络连接池始化参数对象为空！");
		}
		manager = SocketPoolManager.getInstance(vos);
	}
	
	/**
	 * 用网络连接池中的连接输出数据
	 * @param sjob
	 * @param socketPoolName
	 * @param timeout
	 */
	public void printOut(
			String serverId ,
			String socketPoolName ,
			int timeout ,
			byte[] data ,
			SocketJob sjob ){
		PoolSocket soc = null ;
		boolean isExe = false ;
		boolean flag = false ;
		////////////////
		//循环的原因，如果对方服务器强制关闭，那此处创建的连接池中的连接不能得知
		//对方服务器已经关闭，所以连接池中老的连接将可能失效，即使对方服务器马上
		//重新启动，老的连接也可能无效，所以这里应用循环，循环尝试中，如果老的连接
		//通信成功，退出循环，如果老的连接一直不能通信成功，会把老的连接循环一遍，
		//直到新创建的连接，如果新创建的连接通信成功，退出循环，如果不成功，也退出循环。
		do{
			if(timeout == -1 || timeout == 0){
				soc = this.getSocket(socketPoolName) ;
			}else{
				soc = this.getSocket(socketPoolName, timeout) ;
			}
			try{
				sjob.out(serverId , data , soc.out) ;
			}catch(IOException e){
				isExe = true ;
			}finally{
				flag = false ;
				if(isExe){
					isExe = false ;
					if(!soc.newCreate){
						flag = true ;
					}
					this.destroySocket(socketPoolName , soc );
				}else{
					this.freeSocket(socketPoolName , soc) ;
				}
			}
		}while(flag) ;
	}
	
	

	/**
	 * 从池中得到一个socket，当得不到一个socket时，这个方法会阻塞
	 * @param poolName 池名称
	 * @return
	 */
	private PoolSocket getSocket(String poolName){
		return SocketPoolManager.getInstance().getSocket(poolName) ;
	}
	/**
	 * 从池中得到一个socket
	 * @param poolName 池名称
	 * @param timeout 得到一个socket等待最大时长，超过这个时长仍得到不socket，则返回空
	 * @return
	 */
	private PoolSocket getSocket(String poolName , int timeout){
		return SocketPoolManager.getInstance().getSocket(poolName , timeout) ;
	}
	/**
	 * 释放一个socket到池
	 * @param poolName 池名
	 * @param soc socket
	 */
	private void freeSocket(String poolName , PoolSocket soc){
		SocketPoolManager.getInstance().freeSocket(poolName, soc) ;
	}
	/**
	 * 消毁一个无效连接
	 * @param poolName 池名
	 * @param soc socket
	 */
	private void destroySocket(String poolName , PoolSocket soc){
		SocketPoolManager.getInstance().destroySocket(poolName, soc) ;
	}
}
