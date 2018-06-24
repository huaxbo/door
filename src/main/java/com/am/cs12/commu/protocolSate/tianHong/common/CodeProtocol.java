package com.am.cs12.commu.protocolSate.tianHong.common;

import com.am.cs12.commu.protocolSate.tianHong.util.Constant;

public class CodeProtocol {
	
	/**
	 * 得到数据中的功能码
	 * @param b
	 * @return
	 */
	public String parseCode(byte[] b) throws Exception {
		if(b == null){
			throw new Exception("出错，卫星数据为空！") ;
		}
		if(b.length < Constant.Bits_Code ){
			throw new Exception("出错，卫星数据长度不合法，不能取出功能码！") ;
		}
		byte[] cb = new byte[]{b[0], b[1], b[2], b[3], b[4]} ;
		String code = new String(cb) ;
		return code ;
	}
	
	/**
	 * 构造功能码
	 * @param b
	 * @param code
	 * @return
	 */
	public int createCode(byte[] b , String code)throws Exception {
		byte[] cb = code.getBytes() ;
		for(int i = 0 ; i < cb.length ; i++){
			b[i] = cb[i] ;
		}
		return cb.length ;
	}
	
}
