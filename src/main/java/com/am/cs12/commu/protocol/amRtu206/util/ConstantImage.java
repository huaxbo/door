package com.am.cs12.commu.protocol.amRtu206.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class ConstantImage {

	//字节头
	public static final byte HEAD = 0x7E;
	
	public static final int Bits_Head = 2;
	public static final int Bits_Center = 1;
	public static final int Bits_Remote = 5;
	public static final int Bits_Password = 2;
	public static final int Bits_FuncCode = 1;
	public static final int Bits_UdLen = 2;
	public static final int Bits_reportHead = 1;
	public static final int Bits_reportTrail = 1;
	public static final int Bits_CRC = 2;
	//
	public static final int Site_Center_Up = Bits_Head;
	public static final int Site_Remote_Up = Site_Center_Up + Bits_Center;
	public static final int Site_Remote_Down = Site_Center_Up;
	public static final int Site_CneterDown = Site_Remote_Up; 
	public static final int Site_Password = Site_Remote_Up + Bits_Remote;
	public static final int Site_FuncCode =  Site_Password + Bits_Password;//功能码位置
	public static final int Site_UdLen = Site_FuncCode + Bits_FuncCode;
	public static final int Site_ReportHead = Site_UdLen + Bits_UdLen;//报文起始符位置
	public static final int Site_Report = Site_ReportHead + Bits_reportHead;//报文正文位置
	
	//
	public static final int Control_SOH = 0x7E7E;//帧起始
	public static final byte Control_STX = 0x02;//传输正文起始
	public static final byte Control_SYN = 0x16;//多包传输正文起始
	
	public static final byte Control_ETX = 0x03;//报文结束，后续无报文
	public static final byte Control_ETB = 0x17;//报文结束，后续有报文
	
	public static final byte Control_ENQ = 0x05;//询问
	public static final byte Control_ACK = 0x06;//肯定确认，继续发送
	public static final byte Control_NAK = 0x15;//否定应答，反馈重发
	public static final byte Control_EOT = 0x04;//传输结束，退出
	public static final byte Control_ESC = 0x1B;//传输结束，终端保持在线
	//
	public static final int Identify_TT = 0x0F0F0;//观测时间标识符
	public static final int Identify_ST = 0x0F1F1;//测站编码标识符
	public static final int Identify_PW = 0x03;//密码
	public static final int Identify_AI = 0x02;//瞬时气温
	public static final int Identify_C = 0x03;//瞬时水温
	public static final int Identify_DRP = 0xF4;//1小时5分钟时段降水量
	public static final int Identify_DRZ1 = 0xF5;//1小时5分钟间隔水位1
	public static final int Identify_P1 = 0x1A;//1小时时段降水量
	public static final int Identify_P2 = 0x1B;//2小时时段降水量
	public static final int Identify_P3 = 0x1C;//3小时时段降水量
	public static final int Identify_P6 = 0x1D;//6小时时段降水量
	public static final int Identify_P12 = 0x1E;//12小时时段降水量
	public static final int Identify_PD = 0x1F;//日降水量
	public static final int Identify_PJ = 0x20;//当前降水量标识符
	public static final int Identify_PN01 = 0x21;//1分钟时段降水量
	public static final int Identify_PN05 = 0x22;//5分钟时段降水量
	public static final int Identify_PN10 = 0x23;//10分钟时段降水量
	public static final int Identify_PN30 = 0x24;//30分钟时段降水量
	public static final int Identify_PR = 0x25;//暴雨量
	public static final int Identify_PT = 0x26;//降水量累计值标识符
	public static final int Identify_Z = 0x39;//瞬时水位标识符 
	public static final int Identify_Q = 0x27;//瞬时流量标识符
	public static final int Identify_VT = 0x38;//电池电压标识符
	public static final int Identify_PIC = 0xF3;//图片
	public static final int Identify_PT_FIVE = 0xF4;//1小时5分钟降雨量
	public static final int Identify_Z_FIVE = 0xF5;//1小时5分水位
	
	public static final int Identify_M10 = 0x10;//10厘米处土壤含水量
	public static final int Identify_M20 = 0x11;//20厘米处土壤含水量
	public static final int Identify_M30 = 0x12;//30厘米处土壤含水量
	public static final int Identify_M40 = 0x13;//40厘米处土壤含水量
	public static final int Identify_M50 = 0x14;//50厘米处土壤含水量
	public static final int Identify_M60 = 0x15;//60厘米处土壤含水量
	public static final int Identify_M80 = 0x16;//80厘米处土壤含水量
	public static final int Identify_M100 = 0x17;//100厘米处土壤含水量
	
	public static final int Identify_Status = 0x45;//遥测站状态机报警信息
	
	public static final int Identify_extCsq = 0xFF03;//信号强度。扩展标识符
	public static final int Identify_STEP = 0x04;//时间步长标识符
	
	public static final int Identify_Code_Center = 0x01;//中心站地址
	public static final int Identify_Code_Remote = 0x02;//遥测站地址
	public static final int[] Identify_Code_Center_main = new int[]{0x04,0x06,0x08,0x0A};//中心站-主信道类型
	public static final int[] Identify_Code_Center_stock = new int[]{0x05,0x07,0x09,0x0B};//中心站-备信道类型
	
	public static final int Identify_Code_Workmodel = 0x0C;//工作方式
	public static final int Identify_Code_Element = 0x0D;//遥测站要素
	public static final int Identify_Code_CardNum = 0x0F;//遥测站通信设备识别号
	
	public static final int Identify_Code_ReportInterval = 0x20;//定时报时间间隔
	public static final int Identify_Code_IncreaseInterval = 0x21;//加报时间间隔
	public static final int Identify_Code_DayStart = 0x22;//降水量日起始时间
	public static final int Identify_Code_TakeInterval = 0x23;//采样间隔
	public static final int Identify_Code_SaveInterval_WL = 0x24;//水位存储间隔
	public static final int Identify_Code_Resolution_Rain = 0x25;//雨量计分辨率
	public static final int Identify_Code_Resolution_WL = 0x26;//水位计分辨率
	public static final int Identify_Code_IncreaseThreshold = 0x27;//雨量加报阈值
	public static final int Identify_Code_Base_WL_1 = 0x28;//水位基值-1
	public static final int Identify_Code_Modify_WL_1 = 0x30;//水位修正值-1
	public static final int Identify_Code_Increase_WL = 0x38;//加报水位-1
	public static final int Identify_Code_Threshold_WL = 0x40;//加报水位加报阈值	
	
	public static final int Identify_ST_P = 0x50;//降水站
	public static final int Identify_ST_H = 0x48;//河道站
	public static final int Identify_ST_K = 0x4B;//水库（湖泊）站
	
	public static final int Identify_VTA = 0x70;//交流A相电压
	public static final int Identify_VTB = 0x71;//交流B相电压
	public static final int Identify_VTC = 0x72;//交流C相电压	
	public static final int Identify_VIA = 0x73;//交流A相电流
	public static final int Identify_VIB = 0x74;//交流B相电流
	public static final int Identify_VIC = 0x75;//交流C相电流
	
	public static final int Identify_GPS_LTTD = 0X76;//GPS信息-纬度（扩展标识符）
	public static final int Identify_GPS_LGTD = 0X77;//GPS信息-经度（扩展标识符）
	public static final int Identify_GPS_ELEVATION = 0X78;//GPS信息-海拔（扩展标识符）
	
	public static final int centerNum = 1;//中心站地址，范围：1~255
	public static final int Identify_Negative = 0xFF;//负数标识
	
	public static Hashtable<String,Integer> rtuPassword = new Hashtable<String,Integer>(0);//遥测站密码映射表
	public static final int rtuPasswordDefault = 0x0;//遥测站默认初始密码
	public static Hashtable<String,Object[]> commondStageData = new Hashtable<String,Object[]>(0);//遥测时段数据-已回复数据缓存
	
	/******************************/

	static{//启动清除过期缓存任务
		new CleanCommondRtuId().start();
	}
}
/**
 * @author Administrator
 * 情况设置地址缓存
 */
class CleanCommondRtuId extends Thread{
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				sleep(5*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
			synchronized(ConstantImage.commondStageData){
				long local = System.currentTimeMillis();
				Set<Entry<String,Object[]>> st = ConstantImage.commondStageData.entrySet();
				Iterator<Entry<String,Object[]>> it = st.iterator();
				while(it.hasNext()){
					Entry<String,Object[]> o = it.next();
					String key = o.getKey();
					Object[] oo = o.getValue();
					long curr = (Long)oo[1];
					if((local - curr) > 15 * 60 * 1000){
						ConstantImage.commondStageData.remove(key);//缓存超时，清除
					}
				}
			}
		}
	}
	
}
