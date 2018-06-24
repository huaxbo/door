package com.am.cs12.config;

public class AmLogVO {

	//日志文件存储目录(相对目录) -->
	public String logDir ;
	//日志文件最大字节数(KB)
	public int fileMaxSize ;
	//日志文件最大文件数
	public int fileMaxNum ;
	
	public AmLogVO(){
		this.logDir = "amLog/" ;
		this.fileMaxSize = 1000 ;
		this.fileMaxNum = 2 ;
	}

}
