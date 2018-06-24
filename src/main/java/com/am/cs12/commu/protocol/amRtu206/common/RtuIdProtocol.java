package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class RtuIdProtocol {
	
	public byte rtuIdType ;

	/**
	 * 分析的RTU ID
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public String parseRtuId(byte[] bs , int startIndex, int endIndex)throws Exception{
		String id = null ;
		UtilProtocol u = new UtilProtocol() ;
		try{
			String preId = u.BCD2String(bs , startIndex , startIndex + 2 ) ;
			int index = startIndex + 3 ;
			byte[] b = new byte[endIndex - index+1] ;
			int j = 0;
			for(int i = b.length-1 ; i >= 0 ; i--){
				b[j++] = bs[index + i] ;
			}
//			b[0] = (byte)(b[0] ^ ((b[0] >> 5) << 5)) ;
			String afterId = "" + (u.bytes2Int(b, 0, b.length)) ;
			while(afterId.length() < 4){
				afterId = "0" + afterId ;
			}
			id = preId +  afterId;
			
		}catch(Exception e){
			throw new Exception("分析RTU ID时出错!" + e.getMessage() , null) ;
		}
		return id ;
	}
	/**
	 * 创建RTU地址
	 * @param id RTU ID
	 * @param idType  RTU ID类型
	 * @param b 命令字节数组数据
	 * @param idSite RTU ID在数组中的位置
	 * @return
	 * @throws Exception
	 */
	public byte[] createRtuId(byte[] b, String id , byte idType , int idSite)throws Exception{
		rtuIdType = idType ;
		if(id == null || id.trim().equals("")){
			throw new Exception("出错，RTU ID为空，") ;
		}
		id = id.trim() ;
//		if(id.length() != Constant.RTU_ID_len){
//			throw new Exception("出错，RTU ID长度必须是11，实为" + id.length() + "，传来的RTU ID为" + id) ;
//		}

		String cityNo = null ;
		String address = null ;
		if(idType == Constant.RTU_ID_type_RTU_206 || idType == Constant.RTU_ID_type_relay_206){
			//一般ID或中继ID
			cityNo = id.substring(0 , 6) ;
			address = id.substring(6) ;
		}else{
			//广播ID
			cityNo = id.substring(0 , 6) ;
			address = "2097151" ;
		}
		
//		address = idType + address ;
		
		UtilProtocol u = new UtilProtocol() ;
		
		byte[] cb = u.string2BCD(cityNo) ;
		byte[] cityNo_b = new byte[3] ;
		if(cb == null){
			throw new Exception("RTU ID不合法，其城市编号转成BCD编码时出错！" , null) ;
		}else{
			if(cb.length == 1){
				cityNo_b[0] = 0 ;
				cityNo_b[1] = 0 ;
				cityNo_b[2] = cb[0] ;
			}else
			if(cb.length == 2){
				cityNo_b[0] = 0 ;
				cityNo_b[1] = cb[0] ;
				cityNo_b[2] = cb[1] ;
			}else
				if(cb.length == 3){
					cityNo_b[0] = cb[0] ;
					cityNo_b[1] = cb[1] ;
					cityNo_b[2] = cb[2] ;
				}
		}
		
		byte[] ab = u.int2bytes(Integer.parseInt(address)) ;
		byte[] address_b = new byte[2] ;
		if(ab == null){
			throw new Exception("RTU ID不合法，其测控器编号转成BCD编码时出错！" , null) ;
		}else{
			if(ab.length == 1){
				address_b[0] = ab[0] ;
				address_b[1] = 0 ;
			}else
			if(ab.length == 2){
				address_b[0] = ab[1] ;
				address_b[1] = ab[0] ;
			}
		}
		
		
		int n = idSite ;//Constant.Site_RTUID ;
		b[n++] = cityNo_b[0] ;
		b[n++] = cityNo_b[1] ;
		b[n++] = cityNo_b[2] ;
		b[n++] = address_b[0] ;
		b[n++] = address_b[1] ;
//		b[n++] = (byte)0X03 ;
//		b[n++] = (byte)0X00 ;
//		b[n++] = (byte)0X13 ;
//		b[n++] = (byte)0X13 ;
//		b[n++] = (byte)0X04 ;
//		if(idType == Constant.RTU_ID_type_RTU_206){
//			//RTU ID
//			b[n] = (byte)(b[n] | (Constant.RTU_ID_type_RTU_206 << 5)) ;
//		}else if(idType == Constant.RTU_ID_type_relay_206){
//			//中继ID
//			b[n] = (byte)(b[n] | (Constant.RTU_ID_type_relay_206 << 5)) ;
//		}
//		n++ ;
		return b ;
	}
	
}

