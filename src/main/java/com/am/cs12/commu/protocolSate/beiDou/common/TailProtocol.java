package com.am.cs12.commu.protocolSate.beiDou.common;

import com.am.cs12.commu.protocolSate.beiDou.util.Constant;

public class TailProtocol {
	/**
	 * 分析数据尾
	 * @param b
	 */
	public void parseTail(byte[] b) throws Exception{
		int n = b.length - 1 ;
		if(b[n] != Constant.tail_0D_byte){
			throw new Exception("卫星数据倒数据 第一字节不是0X0A") ;
		}
		if(b[n - 1] != Constant.tail_0D_byte){
			throw new Exception("卫星数据倒数据 第二字节不是0X0D") ;
		}
	}
	
	/**
	 * 创建数据尾
	 * @param b
	 * @param len
	 * @return
	 */
	public byte[] createTail(byte[] b) throws Exception{
		b[b.length-1] = Constant.tail_0A_byte ;
		b[b.length-2] = Constant.tail_0D_byte ;
		return b ;
	}

}
