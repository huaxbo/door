package com.am.cs12.config;

import java.util.*;
import com.am.cs12.commu.publish.*;

public class PublishServerVO {

	public String id ;
	public boolean enable ;
	public String driverName ;
	public String configFilePath ;
	public PublishServer server ;
	public PublishServerContext context ;
	//所接受的数据功能码
	//如果为null，则不论何种功能码，全部接受数据
	public List<String> acceptCodes ;
	public PublishServerVO(){
		this.acceptCodes = new ArrayList<String>() ;
	}
}
