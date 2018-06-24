package com.am.cs12.commu.protocolSate.beiDou;


import com.am.cs12.commu.protocol.util.UtilProtocol;

public class CodeBD {
	//
	public static final String $BBXX = "$BBXX" ; 
	//
	public static final String $DWXX = "$DWXX" ; 
	//
	public static final String $FKXX = "$FKXX" ; 
	//
	public static final String $ICXX = "$ICXX" ; 
	//
	public static final String $SJXX = "$SJXX" ; 

	//通信信息
	public static final String $TXXX = "$TXXX" ; 
	//
	public static final String $ZJXX = "$ZJXX" ; 
	
	public static void main(String[] args) throws Exception{
		byte[] b = $TXXX.getBytes() ;
		UtilProtocol u = new UtilProtocol() ;
		String hex = u.byte2Hex(b , true) ;
		
		System.out.println(hex) ;
		
		String s = new String(b) ;
		
		System.out.println(s) ;
	}
}
