package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.util.UtilProtocol;

public class Test {

	public static void main(String[] args) throws Exception{
		UtilProtocol u = new UtilProtocol() ;
		byte[] b = new byte[]{0} ;
		String key = u.BCD2String(b , 0 , 0) ;
		System.out.println(key);
	}
}
