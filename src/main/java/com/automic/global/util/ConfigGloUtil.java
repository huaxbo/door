package com.automic.global.util;

import java.util.List;

import javax.servlet.ServletContext;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigGloUtil {

	private static Element root;
	static{
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(ConfigGloUtil.class.getResource("/config-glo.xml"));
			root = doc.getRootElement();
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取配置项
	 * @param ele
	 * @return
	 */
	public static String getElementText(String ele){
		if(root != null){
			
			return root.elementText(ele);
		}
		
		return null;		
	}	
	
	/**
	 * 初始化
	 * @param sc
	 */
	public static void initConfigGlo(ServletContext sc){
		if(sc != null && root != null){
			List<?> lt = root.elements();
			for(int i=0;i<lt.size();i++){
				Element elt = (Element)lt.get(i);
				sc.setAttribute(elt.getName(), elt.getTextTrim());				
			}
		}		
	}
}
