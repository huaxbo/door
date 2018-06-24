package com.am.cs12.commu.protocol.amRtu206;


public class Code206 {
	///////////////////////////////////////////////
	//功能码都为16进制的字符串
	
	public static final String cd_00 = "00" ;//回还命令，通信服务器内部用，通信协议206中不存在此命令
	
	public static final String cd_01 = "01" ;//确认或否认
	
	public static final String cd_02 = "02" ;//链路检测（AFN=02H）
	
	public static final String cd_10 = "10" ; //设置遥测终端、中继站地址
	public static final String cd_50 = "50" ; //查询遥测终端、中继站地址
	
	public static final String cd_11 = "11" ; //设置遥测终端、中继站时钟
	public static final String cd_51 = "51" ; //查询遥测终端、中继站时钟
	
	public static final String cd_12 = "12" ; //设置遥测终端工作模式
	public static final String cd_52 = "52" ; //查询遥测终端工作模式
	
	public static final String cd_A1 = "A1" ; //设置遥测终端的数据自报种类及时间间隔
	public static final String cd_53 = "53" ; //查询遥测终端的数据自报种类及时间间隔

	public static final String cd_A0 = "A0" ; //设置遥测终端需查询的实时数据种类
	public static final String cd_54 = "54" ; //查询遥测终端需查询的实时数据种类

	public static final String cd_15 = "15" ; //设置遥测终端本次充值量
	public static final String cd_55 = "55" ; //查询遥测终端最近成功充值量和现有剩余水量

	public static final String cd_16 = "16" ; //设置遥测终端剩余水量报警值
	public static final String cd_56 = "56" ; //查询遥测终端剩余水量和报警值

	public static final String cd_17 = "17" ; //设置遥测终端的水位基值、水位上下限
	public static final String cd_57 = "57" ; //查询遥测终端水位基值、水位上下限

	public static final String cd_18 = "18" ; //设置遥测终端水压上、下限
	public static final String cd_58 = "58" ; //查询遥测终端水压上、下限

	public static final String cd_19 = "19" ; //设置遥测终端水质参数种类、上限值
	public static final String cd_59 = "59" ; //查询遥测终端水质参数种类、上限值

	public static final String cd_1A = "1A" ; //设置遥测终端水质参数种类、下限值
	public static final String cd_5A = "5A" ; //查询遥测终端下限值水质参数种类、下限值

	public static final String cd_60 = "60" ; //查询遥测终端转发中继引导码长
	public static final String cd_1C = "1C" ; //设置遥测终端转发中继引导码长
	
	public static final String cd_1B = "1B" ; //设置终端站流量的表底（初始）值


	public static final String cd_62 = "62" ; //查询中继站转发终端地址
	public static final String cd_1D = "1D" ; //设置中继站转发终端地址

	public static final String cd_1E = "1E" ; //设置中继站工作机自动切换，自报状态
	
	public static final String cd_1F = "1F" ; //设置遥测终端流量参数上限值
	
	public static final String cd_B1 = "B1" ; //查询遥测终端固态存储数据
	public static final String cd_20 = "20" ; //设置遥测终端检测参数启报阈值及固态存储时间段间隔

	public static final String cd_30 = "30" ; //置遥测终端IC卡功能有效
	public static final String cd_31 = "31" ; //取消遥测终端IC卡功能
	
	public static final String cd_32 = "32" ; //定值控制投入
	public static final String cd_33 = "33" ; //定值控制退出
	public static final String cd_34 = "34" ; //定值量设定
		
	public static final String cd_B0 = "B0" ; //查询遥测终端的实时值
	public static final String cd_B2 = "B2" ; //查询遥测终端内存自报数据
	public static final String cd_5D = "5D" ; //查询遥测终端的事件记录
	public static final String cd_5E = "5E" ; //查询遥测终端状态和报警状态
	public static final String cd_5F = "5F" ; //查询水泵电机实时工作数据
	
	public static final String cd_61 = "61" ; //查询遥测终端图像记录
	
	public static final String cd_63 = "63" ; //查询中继站工作机状态和切换记录
	public static final String cd_64 = "64" ; //查询遥测终端流量参数上限值
	
	public static final String cd_81 = "81" ; //随机自报报警数据
	public static final String cd_82 = "82" ; //人工置数
	
	public static final String cd_90 = "90" ; //复位遥测终端参数和状态
	
	public static final String cd_91 = "91" ; //清空遥测终端历史数据单元
	
	public static final String cd_92 = "92" ; //遥控启动水泵或阀门/闸门
	public static final String cd_93 = "93" ; //遥控关闭水泵或阀门/闸门
	
	public static final String cd_94 = "94" ; //遥控终端或中继站通信机切换
	public static final String cd_95 = "95" ; //遥控中继站工作机切换
	public static final String cd_96 = "96" ; //修改遥测终端密码
		
	
	public static final String cd_C0 = "C0" ; //遥测终端自报实时数据
	public static final String cd_C1 = "C1" ; //遥测终端自报实时数据(扩展瞬时流量)
	public static final String cd_C5 = "C5" ; //水泵遥测终端自报实时数据(扩展瞬时流量，水位，电流，电压，等)
	public static final String cd_C6 = "C6" ; //扩展室内温度及室内湿度
	
	public static final String cd_85 = "85" ; //设置数据分析时间间隔
	public static final String cd_8B = "8B" ; //查询数据分析时间间隔
	public static final String cd_87 = "87" ; //数据分析启动
	public static final String cd_88 = "88" ; //数据分析关闭
	public static final String cd_89 = "89" ; //反冲洗启动
	public static final String cd_8A = "8A" ; //反冲洗关闭
	
	//中继站扩展命令
	public static final String cd_44 = "44" ; //设置RTU地址
	public static final String cd_74 = "74" ; //查询RTU地址

	public static final String cd_45 = "45" ; //设置LCD显示内容及刷屏间隔
	public static final String cd_75 = "75" ; //查询LCD显示内容及刷屏间隔
	public static final String cd_76 = "76" ; //查询正积流量
	public static final String cd_77 = "77" ; //查询负积流量
	public static final String cd_4C = "4C" ; //lora时间窗口配置
	public static final String cd_7C = "7C" ; //lora时间窗口查询
	
	public static final String cd_40 = "40" ; //设置净积流量
	public static final String cd_70 = "70" ; //查询净积流量
	
	public static final String cd_F1 = "F1";//智能门操作命令
	public static final String cd_F4 = "F4";//智能门状态报
	
	

}
