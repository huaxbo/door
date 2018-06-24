package com.am.cs12.config.conf;

import java.util.Iterator;
import java.util.List;
import java.lang.IllegalArgumentException ;
import org.jdom.Element;

import com.am.util.NumberUtil;
import com.am.cs12.config.SmServerVO ;
import com.am.cs12.util.Config;

public class SmServerConfig  extends Config{
	
	private SmServerVO vo  ;
	
	public SmServerConfig(){
		this.vo = new SmServerVO() ;
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public SmServerVO parseOptions(String filePath) throws IllegalArgumentException {
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
			if(name.equals(SmServerConstant.ID )){
				if(value == null || value.equals("")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.ID + "必须配置！");
				}
				vo.id = value ;
			}
			if(name.equals(SmServerConstant.DEBUGFORNOMODEL)){
				if(value == null || (!value.equals("true") && !value.equals("false")) ){
					throw new IllegalArgumentException(filePath + "中元素"+ SmServerConstant.DEBUGFORNOMODEL + "配置必须是true或false！");
				}
				vo.debugForNoModel = Boolean.parseBoolean(value) ;
			}
			if(name.equals(SmServerConstant.DEVICE )){
				if(value == null || !value.startsWith("COM")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.DEVICE + "必须为COM* ！");
				}
				vo.device = value ;
			}
			if(name.equals(SmServerConstant.BAUDRATE )){
				if(!NumberUtil.isIntNumber(value)){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.BAUDRATE  + "必须为数字！");
				}
				if(Integer.parseInt(value) <= 0){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.BAUDRATE + "必须大于等于0！");
				}
				vo.baudrate = Integer.parseInt(value) ;
			}
			if(name.equals(SmServerConstant.MODELTYPE)){
				if(value == null || value.equals("")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.MODELTYPE + "必须不为空 ！");
				}
				vo.modelType = value ;
			}
			if(name.equals(SmServerConstant.SMID)){
				if(value == null || value.equals("")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.SMID + "必须不为空 ！");
				}
				vo.smId = value ;
			}
			if(name.equals(SmServerConstant.FACTORY)){
				if(value == null || value.equals("")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.FACTORY + "必须不为空 ！");
				}
				vo.factory = value ;
			}
			if(name.equals(SmServerConstant.SN)){
				if(value == null || value.equals("")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + SmServerConstant.SN + "必须不为空 ！");
				}
				vo.sn = value ;
			}
			if(name.equals(SmServerConstant.CODEONLYBYSM)){
				if(value == null || value.equals("")){
				}else{
					value = value.replaceAll("，", ",") ;
					String[] ss = value.split(",") ;
					vo.codeOnlyBySm = new String[ss.length] ;
					for(int i = 0 ; i < ss.length ; i++){
						vo.codeOnlyBySm[i] = ss[i].trim() ;
					}
				}
				
			}
			if(name.equals(SmServerConstant.CODEENABLEBYSM)){
				if(value == null || value.equals("")){
				}else{
					value = value.replaceAll("，", ",") ;
					String[] ss = value.split(",") ;
					vo.codeEnableBySm = new String[ss.length] ;
					for(int i = 0 ; i < ss.length ; i++){
						vo.codeEnableBySm[i] = ss[i].trim() ;
					}
				}
			}
		}
		return vo ;
	}
}

