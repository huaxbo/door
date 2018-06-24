package com.am.cs12.config.conf ;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import java.io.File;
import java.lang.IllegalArgumentException ;

import com.am.cs12.util.*;
import com.am.cs12.config.*;
import com.am.util.* ;
import com.am.cs12.Server;

public class AmLogConfig  extends Config{
	
	private AmLogVO vo  ;
	
	public AmLogConfig(){
		this.vo = new AmLogVO() ;
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AmLogVO parseOptions(String filePath) throws IllegalArgumentException {
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

			if(name.trim().equals(AmLogConstant.logDir)){
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，" + AmLogConstant.logDir + "元素未配置！");
				}else{
					vo.logDir = Server.serverCurrentPath + value + "\\";
					File dir = new File(vo.logDir);
					if (!dir.exists()) {
						dir.mkdir();
					}
				}
			}else if(name.trim().equals(AmLogConstant.fileMaxSize)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ AmLogConstant.fileMaxSize + "配置数值必须是整数！");
				}
				vo.fileMaxSize = Integer.parseInt(value) ;
				if(vo.fileMaxSize < 500){
					vo.fileMaxSize = 500 ;
				}
				if(vo.fileMaxSize > 1000 ){
					vo.fileMaxSize = 1000 ;
				}
				vo.fileMaxSize = vo.fileMaxSize * 1000 ;
			}else if(name.trim().equals(AmLogConstant.fileMaxNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ AmLogConstant.fileMaxNum + "配置数值必须是整数！");
				}
				vo.fileMaxNum = Integer.parseInt(value) ;
				if(vo.fileMaxNum < 2){
					vo.fileMaxNum = 2 ;
				}
				if(vo.fileMaxNum > 10 ){
					vo.fileMaxNum = 10 ;
				}
			}
		}//end while
		return vo ;
	}

}
