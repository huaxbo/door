package com.am.cs12.commu.protocol.amRtu206.util;

public class Constant {
	
	//字节头
	public static final byte HEAD = 0x68 ;
	//字节尾
	public static final byte TAIL = 0x16 ;
	
	public static final byte Site_Control = 3 ;//控制域在RTU数据中的位置，数据是字节数组，数据组位置从0开始
	public static final int Site_RTUID = 4 ;//当无DIVS时，RTU ID在RTU数据中的位置，当有DIVS时，这个值要加1，数据是字节数组，数据组位置从0开始
	public static final int Site_Code = 9 ;//当无DIVS时，功能码在RTU数据中的位置，当有DIVS时，这个值要加1，数据是字节数组，数据组位置从0开始
	/**当无DIVS时，用户数据(不包括功能码)在RTU数据中的位置，当有DIVS时，这个值要加1，数据是字节数组，数据组位置从0开始*/
	public static final int Site_Data = 10 ;

	
	public static final byte DIR_toRtu = 0 ;//下行报文，发向测控终端RTU的数据
	public static final byte DIR_fromRtu = 1 ;//上行报文，从测控终端RTU发来的数据
	
	public static final byte DIV_yes = 1 ;//表示报文已被拆分为若干帧
	public static final byte DIV_no = 0 ;//表示报文未被拆分为若干帧

	public static final byte FCB_Default = 3 ;//FCB默认值
	public static final byte DIVS_Default = 0 ;//DIVS默认值

	
	public static final int RTU_ID_len = 11 ;//RTU ID是14位长的字符串(4位城市编号，7(3个字节二进制最大数是 2097151)位地址)
	
	public static final byte RTU_ID_type_RTU = 0 ;//RTU ID 
	public static final byte RTU_ID_type_relay = 1 ;//中继RTU ID
	public static final byte RTU_ID_type_broadcast = 9 ;//广播地址

	public static final byte RTU_ID_type_RTU_206 = 2 ;//RTU ID 
	public static final byte RTU_ID_type_relay_206 = 5 ;//中继RTU ID

	public static final int Bits_Head = 3 ;//数据头字节数
	public static final int Bits_Len = 1 ;//帧长度字节数
	public static final int Bits_Control = 1 ;//控制域字节数
	public static final int Bits_RTU_ID = 5 ;//RTU ID域字节数
	public static final int Bits_Code = 1 ;//功能码字节数
	public static final int Bits_Password = 2 ;//密钥字节数
	public static final int Bits_Time = 5 ;//时间标签字节数
	public static final int Bits_CRC = 1 ;//CRC域字节数（206协议用的是CRC8，一个字节）
	public static final int Bits_Tail = 1 ;//数据尾字节数
	public static final int Bits_Data = 2 ;//数据长度节数
	
	
	public static final int Flag_f0_forCode2 = 0xF0 ;//登录
	public static final int Flag_f1_forCode2 = 0xF1 ;//退出登录
	public static final int Flag_f2_forCode2 = 0xF2 ;//在线保持
	
	
	public static final byte ControlFunCode_0 = 0 ;//发送∕确认	命令
	public static final byte ControlFunCode_1 = 1 ;//1 查询∕响应帧 雨量参数
	public static final byte ControlFunCode_2 = 2 ;//2 查询∕响应帧 水位参数
	public static final byte ControlFunCode_3 = 3 ;//3 查询∕响应帧 流量(水量)参数
	public static final byte ControlFunCode_4 = 4 ;//4 查询∕响应帧 流速参数
	public static final byte ControlFunCode_5 = 5 ;//5 查询∕响应帧 闸位参数
	public static final byte ControlFunCode_6 = 6 ;//6 查询∕响应帧 功率参数
	public static final byte ControlFunCode_7 = 7 ;//7 查询∕响应帧 气压参数
	public static final byte ControlFunCode_8 = 8 ;//8 查询∕响应帧 风速参数
	public static final byte ControlFunCode_9 = 9 ;//9 查询∕响应帧 水温参数
	public static final byte ControlFunCode_10 = 10 ;//10 查询∕响应帧 水质参数
	public static final byte ControlFunCode_11 = 11 ;//11 查询∕响应帧 土壤含水率参数
	public static final byte ControlFunCode_12 = 12 ;//12 查询∕响应帧 蒸发量参数
	public static final byte ControlFunCode_13 = 13 ;//13 查询∕响应帧 报警或状态参数
	public static final byte ControlFunCode_14 = 14 ;//14 查询∕响应帧 综合参数
	public static final byte ControlFunCode_15 = 15 ;//15 查询∕响应帧 水压参数
	

}
