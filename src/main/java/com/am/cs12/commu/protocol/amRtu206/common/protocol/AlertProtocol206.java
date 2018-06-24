package com.am.cs12.commu.protocol.amRtu206.common.protocol;

import com.am.cs12.commu.protocol.amRtu206.common.data.*;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class AlertProtocol206 {
	
	/*
    Byte1     
    D0—工作交流电停电告警；
	D1—蓄电池电压报警；
	D2—水泵开停自报；
	D3—主板电池电压报警；
	D4—终端箱门状态报警；
	D5-D7—备用。
	Byte2     
	D0—瞬时雨量报警；
	D1— 雨量仪表通讯故障报警；
	D2—水位上下限报警；
	D3—水位仪表故障报警；
	D4—流量上下限报警；
	D5—流量仪表报警；
	D6—水温上下限报警；
	D7—水温仪表报警；
	Byte3     
	D0—水质上下限报警；
	D1—水质仪表报警；
	D2—土壤含水率上下限报警；
	D3—土壤含水率仪表报警；
	D4—蒸发量上下限报警；
	D5—蒸发量仪表报警；
	D6—气温上下限报警；
	D7备用
	Byte4     
	D0—D7备用
	 */
	/**
	 * 分析报警部分
	 */
	public void parse(byte[] b , int index , Data206_alt alertData) throws Exception {
		int n = index ; 
		UtilProtocol u = new UtilProtocol() ;
		int v = u.byte2PlusInt(b[n++]) ;
		
		v = v & 0x1F ;//D5-D7—备用。
		int v4 = (v & 0x10) >> 4 ;
		int v3 = (v & 0x8) >> 3 ;
		int v2 = (v & 0x4) >> 2 ;
		int v1 = (v & 0x2) >> 1 ;
		int v0 = (v & 0x1) ;
		alertData.setPower220Stop_alt(v0) ;//工作交流电停电告警；
		alertData.setStorePowerLowVoltage_alt(v1) ;//蓄电池电压报警；
		alertData.setPumpStartStop_alt(v2) ;//水泵开停自报；
//		alertData.setBoardBatteryLowVoltage_alt(v3) ;//主板电池电压报警；
		alertData.setBoxDoorOpen_alt(v4) ;//终端箱门状态报警；

		v = u.byte2PlusInt(b[n++]) ;
		int v7 = (v & 0x80) >> 7 ;
		int v6 = (v & 0x40) >> 6 ;
		int v5 = (v & 0x20) >> 5 ;
		v4 = (v & 0x10) >> 4 ;
		v3 = (v & 0x8) >> 3 ;
		v2 = (v & 0x4) >> 2 ;
		v1 = (v & 0x2) >> 1 ;
		v0 = (v & 0x1) ;
//		alertData.setInstanceRain_alt(v0) ;//瞬时雨量报警；
//		alertData.setRainMeterCommu_alt(v1) ;//雨量仪表通讯故障报警；
		alertData.setWaterLevel_alt(v2) ;//水位上下限报警；
		alertData.setWaterLevelMeter_alt(v3) ;//水位仪表故障报警；
		alertData.setWaterFlow_alt(v4) ;//流量上下限报警；
		alertData.setWaterFlowMeter_alt(v5) ;//流量仪表报警；
//		alertData.setWaterTemperature_alt(v6) ;//水温上下限报警；
//		alertData.setWaterTemperatureMeter_alt(v7) ;//水温仪表报警；
		

		v = u.byte2PlusInt(b[n++]) ;
		if(v == 170){
			return ;
		}
		v = v & 0x7F ;//D7备用
		v6 = (v & 0x40) >> 6 ;
		v5 = (v & 0x20) >> 5 ;
		v4 = (v & 0x10) >> 4 ;
		v3 = (v & 0x8) >> 3 ;
		v2 = (v & 0x4) >> 2 ;
		v1 = (v & 0x2) >> 1 ;
		v0 = (v & 0x1) ;
		alertData.setWaterQuality_alt(v0) ;//水质上下限报警；；
//		alertData.setWaterQualityMeter_alt(v1) ;//水质仪表报警；
//		alertData.setSoilWater_alt(v2) ;//土壤含水率上下限报警；
//		alertData.setSoilWaterMeter_alt(v3) ;//土壤含水率仪表报警；
//		alertData.setEvaporation_alt(v4) ;//蒸发量上下限报警；
//		alertData.setEvaporationMeter_alt(v5) ;//蒸发量仪表报警；
//		alertData.setTemperature_alt(v6) ;//气温上下限报警；

		//n++ ;//Byte4 D0—D7备用
		
	}

}
