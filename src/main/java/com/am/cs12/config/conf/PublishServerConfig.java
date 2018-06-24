package com.am.cs12.config.conf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.am.util.ResUtil;
import com.am.util.StringUtil;
import com.am.cs12.config.PublishServerVO;
import com.am.cs12.util.*;

public class PublishServerConfig   extends Config {

	private HashMap<String, PublishServerVO> publishServerMap ;
	
	public PublishServerConfig(){
		this.publishServerMap = new HashMap<String, PublishServerVO>() ;
	}

	
	/**
	 * 分析测控器配置
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, PublishServerVO> parseOptions(String filePath) throws IllegalArgumentException {
		if(this.doc == null){
			super.createDom(filePath) ;
		}
		if(this.doc == null){
			throw new IllegalArgumentException(filePath + "语法有错误，不能创建xml文件的doc对象!");
		}
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}
		
		List serverList = root.getChildren(PublishServerConstant.server_key);
		if (serverList == null) {
			return this.publishServerMap ;
		}
		Iterator serverIt = serverList.iterator();
		Element server = null;
		
		String attValue = null ;
		while (serverIt.hasNext()) {
			server = (Element)serverIt.next();
			PublishServerVO serverVo = new PublishServerVO() ;
			
			attValue = server.getAttributeValue(PublishServerConstant.id_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.id_key + "属性未配置！");
			}else{
				if(this.isExistServerId(attValue.trim() , publishServerMap)){
					throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.id_key + "属性重复配置！");
				}
				serverVo.id = attValue.trim() ;
			}
			attValue = server.getAttributeValue(PublishServerConstant.enable_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.enable_key + "属性未配置！");
			}else{
				attValue = attValue.trim() ;
				if(!attValue.equals("true") && !attValue.equals("false")){
					throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.enable_key + "属性值必须是true或false！");
				}
				serverVo.enable = Boolean.parseBoolean(attValue);
			}
			attValue = server.getAttributeValue(PublishServerConstant.driverName_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.driverName_key + "属性必须配置正整数！");
			}else{
				attValue = attValue.trim() ;
				if(!this.checkServerDriver(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.driverName_key + "属性配置不正确！");
				}
				serverVo.driverName = attValue ;
			}
			attValue = server.getAttributeValue(PublishServerConstant.configFilePath_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.configFilePath_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				if(!this.checkServerConfigFilePath(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，server元素的" + PublishServerConstant.configFilePath_key + "属性配置不正确！");
				}
				serverVo.configFilePath = attValue ;
			}
			
			String acceptCodes = server.getTextTrim() ;
			if(acceptCodes == null || acceptCodes.equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，server元素所接受数据的功能码必须配置！");
			}else{
				if(acceptCodes.equalsIgnoreCase("all")){
					//将全部接受
					serverVo.acceptCodes = null ;
				}else{
					String[] ss = StringUtil.splitStringByComma(acceptCodes) ;
					for(int i = -0 ; i < ss.length ; i++){
						serverVo.acceptCodes.add(ss[i]) ;
					}
				}
			}
			
			publishServerMap.put(serverVo.id, serverVo) ;
		}
		return publishServerMap ; 
	}
	/**
	 * 检查RTU的ID是否得复
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	private boolean isExistServerId(String id , HashMap<String, PublishServerVO> publishServerMap) {
		if (publishServerMap.containsKey(id)) {
			return true ;
		}
		return false ;
	}
	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings({ "unchecked", "finally" })
	private boolean checkServerDriver(String driverName) {
		boolean flag = true ;
		try {
			Class c = Class.forName(driverName);
			if (c == null) {
				flag = false ;
			}
		} catch (Exception e) {
			flag = false ;
		}finally{
			return flag ;
		}
	}
	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	private boolean checkServerConfigFilePath(String pathName) {
		URL configFileURL = PublishServerConfig.class.getResource(pathName);
		if(configFileURL == null){
			try {
				configFileURL = new URL(ResUtil.getAppPath(PublishServerConfig.class,pathName));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		if (configFileURL == null) {
			return false ;
		}
		return true ;
	}
	
}
