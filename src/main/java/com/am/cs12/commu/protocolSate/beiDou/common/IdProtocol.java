package com.am.cs12.commu.protocolSate.beiDou.common;

import com.am.cs12.commu.protocolSate.beiDou.util.Constant;

public class IdProtocol {
	/**
	 * 得到数据中的卫星终端ID
	 * @param b
	 * @return
	 */
	public String parseId(byte[] b , int from) throws Exception {
		if(b == null){
			throw new Exception("出错，卫星数据为空！") ;
		}
		if(b.length < from + Constant.Bits_ID ){
			throw new Exception("出错，卫星数据长度不合法，不能取出ID！") ;
		}
		byte[] cb = new byte[]{b[from++], b[from++], b[from++], b[from++], b[from++], b[from++]} ;
		String code = new String(cb) ;
		return code ;
	}
	
	/**
	 * 构造功能码
	 * @param b
	 * @param code
	 * @return
	 */
	public int createId(byte[] b , String id , int from)throws Exception {
		if(id == null){
			throw new Exception("出错，卫星终端ID为空！") ;
		}
		if(id.length() != Constant.Bits_ID ){
			throw new Exception("出错，卫星终端ID长度不合法，不能构造ID！") ;
		}
		for(int i = 0 ; i < id.length() ; i++){
			b[from + i] = (byte)id.charAt(i) ;
		}
		return id.length() ;
	}

}
