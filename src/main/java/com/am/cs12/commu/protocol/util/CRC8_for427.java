package com.am.cs12.commu.protocol.util;

public class CRC8_for427 {
	
	public int CRC8(byte[] b, int from , int end){
	  int crc = 0;
	  for(int i = from ; i <= end ; i++){
		    crc = crc ^ (b[i]);
		    for(int j = 0; j < 8; j++) {
		      if((crc & 0x80)!=0) {
		    	  crc ^= 0xe5;
		    	 crc *= 2;
		      }else{
		    	 crc *= 2;
		      }
		    }
	  }
	  return crc;
	} 
	public static  void main(String asd[]){
		//68 08 68 b0 03 00 13 13 04 02 f0 4a 16
//		byte[] b  ={(byte)0XB0, (byte)0X03, (byte)0X00, (byte)0X13, (byte)0X13, (byte)0X04, (byte)0X02, (byte)0XF0}; 
		//        30 03 00 13 13 04 51
//		byte[] b  ={-80, 3, 0, 19, 19, 4, 2, -16}; 
		byte[] b  ={0x03,0x00,0x13,0x13,0x04,0x51}; 
		int crc = 0;
		  for(int i = 0 ; i <= b.length-1 ; i++){
			    crc = crc ^ (b[i]);
			    for(int j = 0; j < 8; j++) {
			      if((crc & 0x80)!=0) {
			    	  crc *= 2;
			    	  crc ^= 0xE5;
			      }else{
			    	 crc *= 2;
			      }
			    }
		  }
		  System.out.println(crc);
		  System.out.println(crc&0XFF);
	}
}
