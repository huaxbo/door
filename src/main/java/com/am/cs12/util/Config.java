package com.am.cs12.util ;

import java.net.MalformedURLException;
import java.net.URL;
import java.lang.IllegalArgumentException ;

import java.io.*;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import com.am.util.ResUtil;



public abstract class Config {
	
	protected Document doc;

	/**
	 * 创建DOM对象
	 * @param filePath
	 * @throws IllegalArgumentException
	 */
	public String createDom(String filePath) throws IllegalArgumentException {
		URL configFileURL = Config.class.getResource(filePath);
		if(configFileURL == null){
			try {
				configFileURL = new URL(ResUtil.getAppPath(Config.class,filePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		if (configFileURL == null) {
			throw new IllegalArgumentException("没有得到配置文件" + filePath );
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
		return filePath ;
	}
	/**
	 * 创建DOM对象
	 * @param filePath
	 * @throws IllegalArgumentException
	 */
	public Document createDomReDoc(String filePath) throws IllegalArgumentException {
		URL configFileURL = Config.class.getResource(filePath);
		if(configFileURL == null){
			try {
				configFileURL = new URL(ResUtil.getAppPath(Config.class,filePath));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		if (configFileURL == null) {
			throw new IllegalArgumentException("没有得到配置文件" + filePath );
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
		return this.doc ;
	}
	/**
	 * 创建DOM对象
	 * @param filePath
	 * @throws IllegalArgumentException
	 */
	public Document createDomReDoc(File f) throws IllegalArgumentException {
		
		try {
			SAXBuilder sb = new SAXBuilder();
			this.doc = sb.build(f);
		} catch (Exception e) {
			throw new IllegalArgumentException("不能从文件" + f.getName() + "生成DOM对象!");
		}
		if (this.doc == null) {
			throw new IllegalArgumentException("不能从文件" + f.getName() + "生成DOM对象!");
		}
		return this.doc ;
	}
	/**
	 * 分析参数
	 * 
	 * @throws ACException
	 */
	public abstract Object parseOptions(String filePath) throws IllegalArgumentException ;

}
