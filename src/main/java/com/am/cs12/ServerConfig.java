package com.am.cs12;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.am.util.ResUtil;

import java.lang.IllegalArgumentException;

public class ServerConfig {

	private static final String dataSourceServer = "dataSourceServer";
	private static final String remoteServer = "remoteServer";
	private static final String remoteMeterTypeKey = "remoteMeterType";
	private static final String commandSendTypeKey = "commandSendType";
	private static final String centerNum = "centerNum";
	private static final String statusMonitorKey = "statusMonitorEnable";
	private static final String minaLogEnableKey = "minaLogEnable";
	
	public static boolean dataSourceServerEnable = false;
	public static boolean remoteServerEnable = false;
	public static boolean localServerEnable = false;
	public static boolean smServerEnable = false;
	public static boolean serialPortServerEnable = false;
	public static boolean publishServerEnable = false;
	public static boolean webServerEnable = false;
	public static boolean satelliteEnable = false;
	public static boolean pictureEnable = false;
	public static boolean statusMonitorEnable = true;
	public static boolean minaLogEnable = false;
	
	public static int centerNumv=1;
	
	public static String commandSendType = "gprsStat-sm";
	/* 
    <!-- 
    	命令发送模式(gprsStat 无线网络或卫星通信，sm短信通信):
    	    |
    	    |——兼容模式 |——gprsStat优先(配置值为：gprsStat-sm) 
    	    |          |
    	    |          |——sm优先(配置值为：sm-gprsStat)
    	    |——gprsStat模式(配置值为gprsStat)
    	    |
    	    |——sm模式(配置值为sm)
    	 另外，某些命令只能通过短信通道发送，例如唤醒命令
     -->
    */
	public static String gprsStat_sm = "gprsStat-sm";
	public static String sm_gprsStat = "sm-gprsStat";
	public static String gprsStat = "gprsStat";
	public static String sm = "sm";

	private Document doc;

	public void createDom(String filePath) throws IllegalArgumentException {
		URL configFileURL = ServerConfig.class.getResource(filePath);
		if(configFileURL == null){
			try {
				configFileURL = new URL(ResUtil.getAppPath(ServerConfig.class,filePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		if (configFileURL == null) {
			throw new IllegalArgumentException("没有得到" + filePath + "配置!");
		}
		try {
			SAXBuilder sb = new SAXBuilder();
			this.doc = sb.build(configFileURL);
		} catch (Exception e) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能生成DOM对象!");
		}
		if (this.doc == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能生成DOM对象!");
		}
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("rawtypes")
	public void parseOptions(String filePath) throws IllegalArgumentException {
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}

		List list = root.getChildren();
		Iterator it = list.iterator();
		Element e = null;
		String name = null;
		String value = null;
		while (it.hasNext()) {
			e = (Element) it.next();
			name = e.getName();
			value = e.getText();
			if (value == null
					|| ((!value.trim().equals("true")
							&& !value.trim().equals("false") 
							&& !name.equals(remoteMeterTypeKey)
							&& !name.equals(commandSendTypeKey)
							&& !name.equals(centerNum)
							))) {
				throw new IllegalArgumentException("出错！" + filePath
						+ "配置中，各配置项必须为true或false！");
			}
			if (name.equals(dataSourceServer)) {
				dataSourceServerEnable = Boolean.parseBoolean(value);
			} else if (name.equals(remoteServer)) {
				remoteServerEnable = Boolean.parseBoolean(value);
			} else if (name.equals(commandSendTypeKey)) {
				commandSendType = value.trim();
				if (!commandSendType.equals("gprsStat-sm")
						&& !commandSendType.equals("sm-gprsStat")
						&& !commandSendType.equals("gprsStat")
						&& !commandSendType.equals("sm")) {
					commandSendType = "gprsStat-sm";
				}
			}else if(name.equals(centerNum)){
				try{
					centerNumv = Integer.parseInt(value);
					if(centerNumv != -1 && (centerNumv <1 || centerNumv >255)){
						throw new IllegalArgumentException("中心号码必须配置为-1或1~255之间的数值！");
					}
				}catch(Exception cne){
					centerNumv = 1;//默认
				}finally{}
			} else if (name.equals(statusMonitorKey)) {
				statusMonitorEnable = Boolean.parseBoolean(value);
			}else if(name.equals(minaLogEnableKey)){
				minaLogEnable = Boolean.parseBoolean(value);
			}
		}
		this.checkCommandSendType();
	}

	private void checkCommandSendType() throws IllegalArgumentException {
		if (!remoteServerEnable && !smServerEnable) {
			throw new IllegalArgumentException(
					"server.xml文件配置出错，远程GPRS网络通信服务和SM短信通信服务不能同时配置为不启动!");
		}

		if (!remoteServerEnable) {
			// 远程GPRS网络通信服务配置为不启动
			if (commandSendType.equals("gprsStat-sm")
					|| commandSendType.equals("sm-gprsStat")
					|| commandSendType.equals("gprsStat")) {
				throw new IllegalArgumentException(
						"server.xml文件配置出错，远程gprsStat网络通信服务配置为不启动，所以命令发送方式不能配置为gprsStat-sm、sm-gprsStat和gprsStat任何一种!");
			}
		}
		if (!smServerEnable) {
			if (commandSendType.equals("gprsStat-sm")
					|| commandSendType.equals("sm-gprsStat")
					|| commandSendType.equals("sm")) {
				throw new IllegalArgumentException(
						"server.xml文件配置出错，sm短信通信服务配置为不启动，所以命令发送方式不能配置为gprsStat-sm、sm-gprsStat和sm任何一种!");
			}
		}
	}
}
