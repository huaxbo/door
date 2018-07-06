package com.am.cs12.commu.protocol.util;

public class CRC8_for206 {
	
	public int CRC8(byte[] b, int from , int end){
	  int crc = 0;
	  for(int i = from ; i <= end ; i++){
		    crc = crc ^ (b[i]);
		    for(int j = 0; j < 8; j++) {
		      if((crc & 0x80)!=0) {
		    	 crc *= 2;
		    	 crc ^= 0xe5;
		      }else{
		    	 crc *= 2;
		      }
		    }
	  }
	  return crc;
	} 
	
	public static void main(String[] args) {
		byte[] bts = new byte[]{0x68,0x8,0x68,(byte)0xB0, 01, 02, 03, 04, 05, 02,(byte)0xF0,0,0x16};
		CRC8_for206 crc = new CRC8_for206();
		try {
			crc.CRC82(bts,3,bts.length - 3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int CRC82(byte[] b, int from , int end) throws Exception{
		  int crc = 0;
		  UtilProtocol up = new UtilProtocol();
		 System.out.println(from + "::" + end);
		  for(int i = from ; i <= end ; i++){
		  System.out.println(i);
			    crc = crc ^ (b[i]);
			    for(int j = 0; j < 8; j++) {
			      if((crc & 0x80)!=0) {
			    	 crc *= 2;
			    	 crc ^= 0xe5;
			      }else{
			    	 crc *= 2;
			      }
			    }
			    System.out.println(up.byte2Hex(up.int2bytes(crc), false));
		  }
		    System.out.println(up.byte2Hex(up.int2bytes(crc), false));
		  
		  return crc;
		} 
	
}
