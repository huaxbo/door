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
			id = u.BCD2String(bs , startIndex , startIndex + 4 ) ;
			
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
		int len = id.length();
		StringBuilder sder = new StringBuilder(id);
		for(int i = 0;i < 10 - len;i++){
			sder.insert(0, "0");
		}
		byte[] ids = new UtilProtocol().string2BCD(sder.toString());
		int n = Constant.Site_RTUID ;
		for(int i = 0;i < 5 - ids.length;i++){
			n++;
			b[n] = 0;
		}
		for(int i = 0;i < ids.length;i++){
			b[n++] = ids[i];
		}
		
		return b ;
	}
		
}

