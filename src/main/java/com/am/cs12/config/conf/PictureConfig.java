package com.am.cs12.config.conf ;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import java.io.File;
import java.lang.IllegalArgumentException ;

import com.am.cs12.util.*;
import com.am.cs12.config.*;
import com.am.cs12.Server;
import com.am.util.NumberUtil;

public class PictureConfig  extends Config{
	
	private PictureVO vo  ;
	
	public PictureConfig(){
		this.vo = new PictureVO() ;
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PictureVO parseOptions(String filePath) throws IllegalArgumentException {
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

			if(name.trim().equals(PictureConstant.picDir)){
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picDir + "元素未配置！");
				}else{
					vo.picDir = Server.serverCurrentPath + value + "\\";
					File dir = new File(vo.picDir);
					if (!dir.exists()) {
						dir.mkdir();
					}
				}
			}else
				
			if(name.trim().equals(PictureConstant.picIdle)){
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picIdle + "元素必须配置！");
				}else{
					value = value.trim() ;
					if(!NumberUtil.isPlusIntNumber(value)){
						throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picIdle + "元素配置了非正整数！");
					}
					vo.picIdle = Long.parseLong(value) ;
					if(vo.picIdle < 2 || vo.picIdle > 30){
						throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picIdle + "元素取值范围是2-30秒！");
					}else{
						vo.picIdle = vo.picIdle * 1000L ;
					}
				}
			}else
			
			if(name.trim().equals(PictureConstant.picFileLifCycle)){
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picFileLifCycle + "元素必须配置！");
				}else{
					value = value.trim() ;
					if(!NumberUtil.isPlusIntNumber(value)){
						throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picFileLifCycle + "元素配置了非正整数！");
					}
					vo.picFileLifCycle = Long.parseLong(value) ;
					if(vo.picFileLifCycle < 10 || vo.picFileLifCycle > 600){
						throw new IllegalArgumentException(filePath + "配置有错误，" + PictureConstant.picFileLifCycle + "元素取值范围是10-600分钟！");
					}else{
						vo.picFileLifCycle = vo.picFileLifCycle * 60 * 1000L ;
					}
				}
			}else

			if(name.trim().equals(PictureConstant.pic_maxThreadNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ PictureConstant.pic_maxThreadNum + "配置数值必须是整数！");
				}
				vo.pic_maxThreadNum = Integer.parseInt(value) ;
				if(vo.pic_maxThreadNum < 1){
					vo.pic_maxThreadNum = 1 ;
				}
				if(vo.pic_maxThreadNum > 100 ){
					vo.pic_maxThreadNum = 100 ;
				}
			}else
			
			if(name.trim().equals(PictureConstant.pic_minThreadNum)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ PictureConstant.pic_minThreadNum + "配置数值必须是整数！");
				}
				vo.pic_minThreadNum = Integer.parseInt(value) ;
				if(vo.pic_minThreadNum < 1){
					vo.pic_minThreadNum = 1 ;
				}
				if(vo.pic_minThreadNum > 100 ){
					vo.pic_minThreadNum = 100 ;
				}
			}else
			
			if(name.trim().equals(PictureConstant.pic_freeTimeout)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ PictureConstant.pic_freeTimeout + "配置数值必须是整数！");
				}
				vo.pic_freeTimeout = Integer.parseInt(value) ;
				if(vo.pic_freeTimeout < 1000){
					vo.pic_freeTimeout = 1000 ;
				}
			}else
			
			if(name.trim().equals(PictureConstant.pic_busyTimeout)){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException(filePath + "中元素"+ PictureConstant.pic_busyTimeout + "配置数值必须是整数！");
				}
				vo.pic_busyTimeout = Integer.parseInt(value) ;
				if(vo.pic_busyTimeout < 1000){
					vo.pic_busyTimeout = 1000 ;
				}
			}
			
			
			
			
			
			
			
			
		}
		return vo;
	}

}
