package com.am.cs12.commu.local.client;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.command.MinaData;
import com.am.cs12.commu.local.client.connectPool.* ;
import com.am.util.NumberUtil;




@SuppressWarnings("unchecked")
public class MinaClient {

	private String host ;
	private int port ;
	//等待建网络连接从而取得会话的最大时间，超过时长，即返回空会话
	private static int connectTimeout = 3000 ;
	//发出命令后，等待信息发布服务器处理命令返回的结果最在时长，超过时长，即返回空结果
	private static int waitDataTimeout = 10000 ;
	static{
		URL configFileURL = MinaClient.class.getResource("MinaClientConfig.xml");
		if (configFileURL != null) {
			try {
				SAXBuilder sb = new SAXBuilder();
				if(sb != null){
					Document doc = sb.build(configFileURL);
					if(doc != null){
						Element root = doc.getRootElement();
						if (root != null) {
							List list = root.getChildren();
							Iterator it = list.iterator();
							Element e = null;
							String name = null ;
							String value = null ;
							while (it.hasNext()) {
								e = (Element) it.next();
								name = e.getName() ;
								value = e.getText() ;
								if(value != null){
									value = value.trim() ;
								}
								if(name.equals("connectTimeout")){
									if(NumberUtil.isPlusIntNumber(value)){
										connectTimeout = Integer.parseInt(value) ;
										if(connectTimeout < 1000 || connectTimeout > 10000){
											connectTimeout = 3000 ;
										}
									}
								}
								if(name.equals("waitDataTimeout")){
									if(NumberUtil.isPlusIntNumber(value)){
										waitDataTimeout = Integer.parseInt(value) ;
										if(waitDataTimeout < 5000 || waitDataTimeout > 10000){
											waitDataTimeout = 10000 ;
										}
									}
								}
							}
						}
					}
				}
			}catch(Exception e){
				;
			}finally{
				;
			}
		}
	}

	/**
	 * 构造方法
	 * @param host 信息发布服务器IP
	 * @param port 信息发布服务器端口
	 */
	public MinaClient(String host , int port ){
		this.host = host ;
		this.port = port ;
	}
	
	/**
	 * 发送命令并取得命令结果，可能因为网络环境不好，
	 * 网速慢，从而造成得不到网络联接(创建联接不成功或等待创建联接超时)，
	 * 或得到了网络联接，但未得到命令结果(等待命令结果超时或其他原因)
	 * @param minaData 命令
	 * @return 命令结果
	 */
	public MinaData sendAndAnswer(MinaData minaData) {
		IoSession se = null ;
		MinaHandler handler = null ;
		MinaData reCom = null ;
		try {
			se = MinaConnectPool.getSession(host, port, connectTimeout);
			if(se == null){
				return null ;
			}
			handler = (MinaHandler)se.getHandler() ;
			if(handler != null){
				//发送命令
				handler.sendCommand(se, minaData) ;
				//得到命令结果
				reCom = handler.getAnswer(waitDataTimeout) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(se != null){
				//必须执行此语句
				MinaConnectPool.freeSession(host, port, se) ;
			}
		}
		return reCom ;
	}

}
