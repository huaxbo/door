package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class CodeProtocol {
	/**
	 * 得到数据中的功能码(十六进制)
	 * @param b
	 * @return
	 */
	public String parseCode(byte[] b , int fromSite) throws Exception {
		if(b == null){
			throw new Exception("出错，RTU数据为空！") ;
		}
		if(b.length < fromSite){
			throw new Exception("出错，RTU数据长度不合法，不能取出功能码！") ;
		}
		return new UtilProtocol().byte2Hex(new byte[]{b[fromSite]}, false) ;
	}
	
	/**
	 * 构造功能码
	 * @param b
	 * @param code
	 * @return
	 */
	public byte[] createCode(byte[] b , String code, int fromSite)throws Exception {
		if(b == null){
			throw new Exception("出错，RTU命令数据为空！") ;
		}
		if(b.length < Constant.Site_Code + 2){
			throw new Exception("出错，RTU命令数据长度不合法，不能构造命令！") ;
		}
		UtilProtocol u = new UtilProtocol() ;
		int codei = u.hex2Int(code) ;
		b[fromSite] = (byte)(codei) ;
		return b ;
	}
	
}
