package com.am.cs12.config.conf ;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import java.lang.IllegalArgumentException ;

import com.am.cs12.util.*;
import com.am.cs12.config.*;
import com.am.util.* ;

public class CoreServerConfig  extends Config{
	
	private CoreServerVO vo  ;
	
	public CoreServerConfig(){
		this.vo = new CoreServerVO() ;
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CoreServerVO parseOptions(String filePath) throws IllegalArgumentException {
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
			if(name.trim().equals(CoreServerConstant.remote_maxThreadNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.remote_maxThreadNum + "配置数值必须是整数！");
				}
				vo.remote_maxThreadNum = Integer.parseInt(value) ;
				if(vo.remote_maxThreadNum < 1){
					vo.remote_maxThreadNum = 1 ;
				}
				if(vo.remote_maxThreadNum > 100 ){
					vo.remote_maxThreadNum = 100 ;
				}
			}else if(name.trim().equals(CoreServerConstant.remote_minThreadNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.remote_minThreadNum + "配置数值必须是整数！");
				}
				vo.remote_minThreadNum = Integer.parseInt(value) ;
				if(vo.remote_minThreadNum < 1){
					vo.remote_minThreadNum = 1 ;
				}
				if(vo.remote_minThreadNum > 100 ){
					vo.remote_minThreadNum = 100 ;
				}
			}else if(name.trim().equals(CoreServerConstant.remote_freeTimeout)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.remote_freeTimeout + "配置数值必须是整数！");
				}
				vo.remote_freeTimeout = Integer.parseInt(value) ;
				if(vo.remote_freeTimeout < 1000){
					vo.remote_freeTimeout = 1000 ;
				}
			}else if(name.trim().equals(CoreServerConstant.remote_busyTimeout)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.remote_busyTimeout + "配置数值必须是整数！");
				}
				vo.remote_busyTimeout = Integer.parseInt(value) ;
				if(vo.remote_busyTimeout < 1000){
					vo.remote_busyTimeout = 1000 ;
				}
			}else
				
			if(name.trim().equals(CoreServerConstant.cachCom_maxThreadNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.cachCom_maxThreadNum + "配置数值必须是整数！");
				}
				vo.cachCom_maxThreadNum = Integer.parseInt(value) ;
				if(vo.cachCom_maxThreadNum < 1){
					vo.cachCom_maxThreadNum = 1 ;
				}
				if(vo.cachCom_maxThreadNum > 100 ){
					vo.cachCom_maxThreadNum = 100 ;
				}
			}else if(name.trim().equals(CoreServerConstant.cachCom_minThreadNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.cachCom_minThreadNum + "配置数值必须是整数！");
				}
				vo.cachCom_minThreadNum = Integer.parseInt(value) ;
				if(vo.cachCom_minThreadNum < 1){
					vo.cachCom_minThreadNum = 1 ;
				}
				if(vo.cachCom_minThreadNum > 100 ){
					vo.cachCom_minThreadNum = 100 ;
				}
			}else if(name.trim().equals(CoreServerConstant.cachCom_freeTimeout)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.cachCom_freeTimeout + "配置数值必须是整数！");
				}
				vo.cachCom_freeTimeout = Integer.parseInt(value) ;
				if(vo.cachCom_freeTimeout < 1000){
					vo.cachCom_freeTimeout = 1000 ;
				}
			}else if(name.trim().equals(CoreServerConstant.cachCom_busyTimeout)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ CoreServerConstant.cachCom_busyTimeout + "配置数值必须是整数！");
				}
				vo.cachCom_busyTimeout = Integer.parseInt(value) ;
				if(vo.cachCom_busyTimeout < 1000){
					vo.cachCom_busyTimeout = 1000 ;
				}
			}
		}//end while
		if(vo.cachCom_maxThreadNum < vo.cachCom_minThreadNum){
			vo.cachCom_maxThreadNum = vo.cachCom_minThreadNum ;
		}
		return vo ;
	}

}
