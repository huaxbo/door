package com.am.cs12.commu.protocolSate.tianHong.common;


public class LenProtocol {
	
	/**
	 * 构造 
	 * @param b
	 * @param code
	 * @return
	 */
	public int createLen(byte[] b , int len , int from)throws Exception {
		String lenStr = "" + len ;
		for(int i = 0 ; i < lenStr.length() ; i++){
			b[from + i] = (byte)lenStr.charAt(i) ;
		}
		return lenStr.length() ;
	}
	
	
	
	public static void main(String[] args){
		byte[] b = new byte[]{0x24,0x43,0x4F,0x55,0x54,0x2C,0x30,0x2C,0x31,0x2C,0x30,0x2C,0x32,0x31,0x38,0x37,0x30,0x31,0x2C,0x30,0x2C,0x31,0x34,0x30,0x2C,0x35,0x2C,0x34,0x35,0x36,0x36,0x37,0x2C,0x3F,0x0D,0x0A};
		String s = new String(b) ;
		String[] ss = s.split(",") ;
		//$COUT,0,1,0,218701,0,140,5,45667,?
		System.out.println(s) ;
		for(int i = 0 ; i < ss.length ; i++){
			System.out.print(ss[i] + " ") ;
		}
		System.out.println() ;
	}

}
