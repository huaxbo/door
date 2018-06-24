package com.am.cs12.commu.protocolSate.tianHong.$TAPP;

import com.am.cs12.commu.protocolSate.CommandSate;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;
import com.am.cs12.commu.protocolSate.tianHong.common.CRCProtocol;
import com.am.cs12.commu.protocolSate.tianHong.common.CodeProtocol;
import com.am.cs12.commu.protocolSate.tianHong.common.TailProtocol;
import com.am.cs12.commu.protocolSate.tianHong.util.Constant;

public class Read_TAPP {
	private static int len = // 数据域长度
		Constant.Bits_Code + 
		Constant.Bits_CRC + 
		Constant.Bits_Tail +
		1;//一个逗号

	/**
	 * 构造命令
	 * @param com
	 * @return
	 * @throws Exception
	 */
	public byte[] remoteData(CommandSate com) throws Exception {
		// ///////////////////////////
		// 构造数据
		byte[] b = new byte[len];
	
		// 数据功能码
		int n = new CodeProtocol().createCode(b, CodeTH.$TAPP);
		
		b[n++] = ',' ; 
	
		//异或校验
		b[n++] = new CRCProtocol().getCrc(b, 0, len - (Constant.Bits_CRC + Constant.Bits_Tail)) ;
	
		//数据尾
		b = new TailProtocol().createTail(b) ;
		return b;
	}

}
