package com.am.cs12.commu.protocolSate.tianHong.util;

public class Constant {

	public static final String tail_0D_hex = "0D" ;//数据尾字
	public static final String tail_0A_hex = "0A" ;//数据尾字
	public static final byte tail_0D_byte = 0x0D ;//数据尾字
	public static final byte tail_0A_byte = 0x0A ;//数据尾字

	public static final int Bits_Code = 5 ;//功能码字节数
	public static final int Bits_Tail = 2 ;//数据尾字节数
	public static final int Bits_ID = 6 ;//用户(终端)地址(ID)字节数
	public static final int Bits_CRC = 1 ;//异或校验字节数
	

}
