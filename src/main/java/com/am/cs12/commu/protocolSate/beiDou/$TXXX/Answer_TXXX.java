package com.am.cs12.commu.protocolSate.beiDou.$TXXX;


import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.commu.protocolSate.DataSate;
import com.am.cs12.commu.protocolSate.beiDou.CodeBD;

public class Answer_TXXX {
	
	private static Logger log = LogManager.getLogger(Answer_TXXX.class);
	
	public int otherbit = 20;
	
	public int datastat = 18;//4.0协议中电文内容起始位置
	
	/**
	 * 分析数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public DataSate parseData(byte[] b) throws Exception {
		
		
		UtilProtocol u = new UtilProtocol() ;
		
		String hex = new UtilProtocol().byte2Hex(b , true) ;
		DataSate d = new DataSate() ;
		
		String [] ss = hex.split(""); 
		
		d.setCode(CodeBD.$TXXX) ;//功能码
		
        int tlen = u.bytes2Int(b, 5, 2);//总长度   

       /* b[7] &= 0x1F; //效位为低21bit，高3bit填“0”
        String id = u.bytes2Int(b, 7, 3) + ""; 
        
        d.setAddr(id);*/
        //信息类别
        String type = u.bytes2Int(b, 10, 1) + "";        
        d.setType(type);
        //发送方地址
        b[11] &= 0x1F; //效位为低21bit，高3bit填“0”
        String id = u.bytes2Int(b, 11, 3) + "";         
        d.setIdSate(id);
        
		int len = tlen - otherbit;   //电文内容长度
		
		byte[] db = new byte[len] ;
		
		int dataSite = 0 ;
		for(int i = 0 ; i <= 17 ; i++){
			dataSite += ss[i].length() + 1 ;
		}
		
		int count = 0 ;
		for(int i = datastat ; i < datastat + len ; i++){
			db[count++] = b[i] ;
		}
		
		Data_TXXX dd = new Data_TXXX() ;
		
		d.setSubData(dd) ;
		dd.setData(db) ;

		log.info("分析卫星终端上报测控数据:" + d.getSubData().toString());
		return d;
	}


//	public static void main(String[] args) throws Exception{
//		byte b = 98;
//		System.out.println((char)b);
//		byte[] bb = new byte[]{0x31,0x32,0x33};
//		bb[0] &= 0x1F;
//		UtilProtocol u = new UtilProtocol() ;
//	
//		String hex = new UtilProtocol().byte2Hex(bb , true) ;
//		String [] ss = hex.split(""); 
//		
//		int count=0;
//		for (int i=0;i<=2;i++){
//			count += ss[i].length();
//		}
//		System.out.println("count:"+count);	
//		
//		System.out.println(u.bytes2Int(bb, 0, 3) + "");
//		
//	}
	
}
