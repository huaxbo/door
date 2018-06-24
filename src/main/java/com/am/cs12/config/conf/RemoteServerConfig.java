package com.am.cs12.config.conf ;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import java.lang.IllegalArgumentException ;

import com.am.cs12.config.*;
import com.am.util.* ;
import com.am.cs12.util.*;

public class RemoteServerConfig  extends Config{
	
	private RemoteServerVO vo  ;
	
	public RemoteServerConfig(){
		this.vo = new RemoteServerVO() ;
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RemoteServerVO parseOptions(String filePath) throws IllegalArgumentException {
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}
		
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
			if(name.trim().equals(RemoteServerConstant.MINASERVERID)){
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "中元素"+ RemoteServerConstant.MINASERVERID + "配置数值必须配置！");
				}
				vo.id = value ;
			}else if(name.trim().equals(RemoteServerConstant.MINASERVERPORT)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ RemoteServerConstant.MINASERVERPORT + "配置数值必须是数字！");
				}
				if(Long.parseLong(value) > 65535 || Long.parseLong(value) < 1){
					throw new IllegalArgumentException(filePath + "中元素"+ RemoteServerConstant.MINASERVERPORT + "配置数值必须在1-65535之间！");
				}
				vo.port = Integer.parseInt(value) ;
			}else if(name.trim().equals(RemoteServerConstant.MINAIDLE)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ RemoteServerConstant.MINAIDLE + "配置数值必须是数字！");
				}
				if(Integer.parseInt(value) < 3){
					value="3" ;
				}
				if(Integer.parseInt(value) > 60 ){
					value="60" ;
				}
				vo.idle = Integer.parseInt(value) ;
			}else if(name.trim().equals(RemoteServerConstant.MINAPROCESSORS)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ RemoteServerConstant.MINAPROCESSORS + "配置数值必须是数字！");
				}
				if(Integer.parseInt(value) < 3){
					value="3" ;
				}
				if(Integer.parseInt(value) > 10 ){
					value="10" ;
				}
				vo.processors = Integer.parseInt(value) ;
			}else if(name.trim().equals(RemoteServerConstant.MINASERVERCONNECTFOREVER)){
				if(value == null || (!value.equals("true") && !value.equals("false")) ){
					throw new IllegalArgumentException(filePath + "中元素"+ RemoteServerConstant.MINASERVERCONNECTFOREVER + "配置必须是true或false！");
				}
				vo.connectForever = Boolean.parseBoolean(value) ;
			}
		}
		return vo ;
	}

}
