package com.am.cs12.commu.protocol.amRtu206.common.protocol;

import com.am.cs12.commu.protocol.amRtu206.common.data.*;

public class WorkModelProtocol206 {
	
	/**
	 * 分析工作模式部分
	 */
	public Integer parse(byte[] b , int index, Data206_altModel d) throws Exception {
		int n = index ; 
		int model = b[n] ;
		model = model & 0x03 ;//硬件要求
		if(model == 0){
			d.setModelCompatible(true) ;
		}else if(model == 1){
			d.setModelAutoReport(true) ;
		}else if(model == 2){
			d.setModelReadAnswer(true); 
		}
		return model ;
	}

}
