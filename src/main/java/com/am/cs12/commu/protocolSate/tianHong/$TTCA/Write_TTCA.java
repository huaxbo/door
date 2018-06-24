package com.am.cs12.commu.protocolSate.tianHong.$TTCA;

import java.util.HashMap;

import com.am.cs12.commu.protocolSate.CommandSate;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;
import com.am.cs12.commu.protocolSate.tianHong.common.CRCProtocol;
import com.am.cs12.commu.protocolSate.tianHong.common.TailProtocol;
import com.am.cs12.commu.protocolSate.tianHong.util.Constant;
import com.am.cs12.commu.protocolSate.tianHong.common.* ;

public class Write_TTCA {
	private static int len = // 数据域长度
		Constant.Bits_Code + 
		Constant.Bits_CRC + 
		Constant.Bits_ID + 
		Constant.Bits_Tail +
		1 + //发信方ID（方式）
		1 + //保密要求
		1 + //回执标志
		7 ;  //7个逗号

	/**
	 * 构造命令---叫卫星终端发送数据
	 * @param com
	 * @return
	 * @throws Exception
	 */
	public byte[] remoteData(CommandSate com) throws Exception {
		HashMap<String, Object> params = com.getParams() ;
		Param_TTCA param = (Param_TTCA)params.get(Param_TTCA.KEY) ;
		if(param == null ){
			throw new Exception("出错，未提供参数Bean！") ;
		}
		
		String idSate = param.getIdSate() ;
		byte[] data = param.getData() ;
		
		if(idSate == null){
			throw new Exception("出错，未提供远程卫星终端ID！") ;
		}
		if(data == null || data.length == 0){
			throw new Exception("出错，未提供发向远程测控终端的数据！") ;
		}
		
		int dataLen = data.length ;
		
		int thisLen = len + dataLen ;
		
		if(dataLen < 10){
			thisLen += 1 ;
		}else if(dataLen < 100){
			thisLen += 2 ;
		}else{
			thisLen += 3 ;
		}
		
		// ///////////////////////////
		// 构造数据
		byte[] b = new byte[thisLen];

		// 数据功能码
		int n = new CodeProtocol().createCode(b, CodeTH.$TTCA);
		
		b[n++] = ',' ; 
		b[n++] = '1' ;//本机ID
		b[n++] = ',' ; 
		n += new IdProtocol().createId(b, idSate, n) ;
		b[n++] = ',' ; 
		b[n++] = '1' ;//保密要求
		b[n++] = ',' ; 
		b[n++] = '0' ;//回执标志
		b[n++] = ',' ; 

		n += new LenProtocol().createLen(b, dataLen, n) ;
		b[n++] = ',' ; 
		
		for(int i = 0 ; i < data.length ; i++){
			b[n++] = data[i] ;
		}
		b[n++] = ',' ; 
		
		//异或校验
		b[n++] = new CRCProtocol().getCrc(b, 0, thisLen - (Constant.Bits_CRC + Constant.Bits_Tail)) ;

		//数据尾
		b = new TailProtocol().createTail(b) ;
		return b;
	}
	
	
	public static void main(String[] args) throws Exception{
		CommandSate com = new CommandSate() ;
		HashMap<String, Object> params = new HashMap<String, Object>() ;
		
		Param_TTCA p = new Param_TTCA() ;
		p.setIdSate("987654") ;
		p.setData(new byte[]{(byte)0x66,(byte)0x41,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x68,0x48}) ;
		params.put(Param_TTCA.KEY, p) ;
		com.setParams(params) ;
		
		Write_TTCA test = new Write_TTCA() ;
		byte[] b = test.remoteData(com) ;
		String s = new String(b) ;
		System.out.println(s) ;
	}

}
