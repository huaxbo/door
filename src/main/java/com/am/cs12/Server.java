package com.am.cs12;

import java.io.File;
import java.io.IOException;

import com.am.cs12.commu.core.CoreServer;
import com.am.cs12.commu.remote_gprs.RemoteContext;
import com.am.cs12.commu.remote_gprs.RemoteServer;
import com.am.cs12.config.ConfigCenter;

public class Server {
	@SuppressWarnings("unused")
	private static RemoteContext remoteContext = null ;
	private static boolean started = false ;
	private static boolean startException = false ;

	public static String serverCurrentPath = System.getProperty("user.dir") ;
	static{
		if(serverCurrentPath == null || serverCurrentPath.equals("")){
			if(File.separator.equals("/")){
				serverCurrentPath = "/usr/local/";
			}else{
				serverCurrentPath = "C:\\" ;
			}
		}
		if(!serverCurrentPath.equals(File.separator)){
			serverCurrentPath += File.separator;
		}
		
		//log4j.properties文件中引用
		System.setProperty("WORKDIR" , serverCurrentPath) ;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		try {
			new Server().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 启动服务器
	 * 
	 */
	public void start()  throws Exception {
		long startTime = System.currentTimeMillis();
		boolean flag = true;
		// 首先初始化服务器--lecence等

		/*int domain = this.checkLecence() ;
		if(domain == -1){
			//验证未通过
			flag = false ;
		}else{
			com.am.cs12.config.ConfigCenter.maxMeterNumber = domain ;
		}*/
		if (flag){
			this.startServers() ;
			//////////////////////////
			while(!Server.started){
				try{
					Thread.sleep(100) ;
				}catch(Exception e){
				}
			}
		}
		
		
		if (!flag) {
			this.printMessage("奥特美克通信服务器启动失败！");
			this.waitCloseWindow();
		}else{
			if(!Server.startException){
				this.printMessage("奥特美克通信服务器启动用时" + (System.currentTimeMillis() - startTime) + "毫秒") ;
			}else{
				this.printMessage("奥特美克通信服务器启动失败!") ;
			}
		}
	}
	
	/**
	 * 进行lecence验证，验证通过则返回允许接入的最大测控终数，验证未通过，则返回-1
	 * @return
	 */
	public int checkLecence(){
		int domain = -1 ;
		
		return domain;
	}
	
	/**
	 * 启动服务器失败时，输出一些消息
	 * 
	 * @param message
	 */
	private void printMessage(String message) {
		System.out.println(message);
	}
	/**
	 * 等待关闭窗口
	 */
	private void waitCloseWindow() {
		this.printMessage("按任意键关闭窗口！");
		try {
			System.in.read() ;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * 启动服务器的各个服务
	 */
	private void startServers(){
		//启动服务
		new StartSubServerThread().start() ;
	}
	/**
	 * 启动子服务的线程
	 * 
	 * @author Administrator
	 * 
	 */
	private class StartSubServerThread extends Thread {

		public void run() {
			boolean flag = true ;
			try{
				flag = this.startServer();
			}catch(Exception e){
				Server.startException = true ;
				e.printStackTrace() ;
				System.out.println(e.getMessage()) ;
			}finally{
				Server.started = true ;
				if(!flag){
					Server.startException = true ;
				}
			}
		}

		/**
		 * 启动服务
		 * 
		 * @param vo
		 */
		private boolean startServer() throws Exception {
			ServerConfig con = new ServerConfig() ;
			con.createDom("/config/server.xml") ;
			con.parseOptions("/config/server.xml") ;
			
			boolean flag = true ;
			//初始化配置
			flag = ConfigCenter.instance().init() ;
			if(flag){

				new CoreServer().start("coreServer");

			
				if(ServerConfig.remoteServerEnable){
					remoteContext = new RemoteServer().start();
				}else{
					System.err.println("!!远程通信服务配置为不启动。");
				}
				
			}
			return flag ;
		}

	}
}
