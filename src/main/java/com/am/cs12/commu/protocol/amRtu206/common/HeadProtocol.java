package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class HeadProtocol {
	
	public static int len_noUserData = Constant.Bits_Head + //非用户数据域长度
										Constant.Bits_CRC + 
										Constant.Bits_Tail  ;

	
	/**
	 * 得到本协议的数据长度
	 * @param b
	 * @return
	 * @throws Exception
	 */
//	public int getDataLen(byte[] b) throws Exception{
//		int n = 0 ;
//		if(b[n++] != Constant.HEAD){
//			throw new Exception("RTU数据头第一字节不是68H，不能得到数据长度！") ;
//		}
//		if(b.length < 3){
//			throw new Exception("RTU数据长度小于3，不能得到数据长度！") ;
//		}
//		UtilProtocol u = new UtilProtocol() ;
//		int len = u.bytes2Int(b, n, Constant.Bits_Len) ;
//		return len ;
//	}
	
	/**
	 * 分析数据头
	 * @param b
	 */
	public int checkHeadAndGetDataLen(byte[] b) throws Exception{
		int n = 0 ;
		if(b[n++] != Constant.HEAD){
			throw new Exception("RTU数据头第一字节不是68H") ;
		}
		UtilProtocol u = new UtilProtocol() ;
		int len = u.bytes2Int(b, n, Constant.Bits_Len) ;
		if(len != 0 && len > b.length - len_noUserData){
			throw new Exception("RTU数据标识长度(" + len + ")大于实际长度(" + b.length + ")。!") ;
		}
		n++;
		if(b[n++] != Constant.HEAD){
			throw new Exception("RTU数据头第四字节不是68H") ;
		}
		return len ;
	}
	
	/**
	 * 分析图像数据头
	 * @param b
	 */
	public int checkImageDataLen(byte[] b) throws Exception{
		int n = 0 ;
		if(b[n++] != Constant.HEAD){
			throw new Exception("RTU数据头第一字节不是68H") ;
		}
		int len = CommonProtocol.getLenForPic(b);
		if(len != 0 && len > b.length - len_noUserData){
			throw new Exception("RTU数据标识长度(" + len + ")大于实际长度(" + b.length + ")。!") ;
		}
		n++;
		if(b[n++] != Constant.HEAD){
			throw new Exception("RTU数据头第四字节不是68H") ;
		}
		return len ;
	}
	
	/**
	 * 创建数据头
	 * @param b
	 * @param len
	 * @return
	 */
	public byte[] createHead(byte[] b , int len) throws Exception{
		UtilProtocol u = new UtilProtocol() ;
		
		int n = 0 ; 
		b[n++] = Constant.HEAD ;
		
		byte[] blen = u.int2bytes(len - len_noUserData)  ;
		b[n++] = blen[0] ;
		
		b[n++] = Constant.HEAD ;
		
		return b ;
	}

}
