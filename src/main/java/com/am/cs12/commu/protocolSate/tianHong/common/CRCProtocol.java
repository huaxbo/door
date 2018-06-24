package com.am.cs12.commu.protocolSate.tianHong.common;

public class CRCProtocol {
	/**
	 * 异或校验
	 * @return
	 */
	public byte getCrc(byte[] b , int from , int len){
		int n = 0 ;
		for(int i = from ; i < len ; i++){
			n = n ^ b[i] ;
		}
		return (byte)n ;
	}
}
