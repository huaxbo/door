package com.am.cs12.commu.protocol.amRtu206.common.protocol;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.amRtu206.common.data.*;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class DatumProtocol206 {
	private static Logger log = LogManager.getLogger(DatumProtocol206.class);
	
	private int dataBytes ;//数据总字节数据
	private Double rainCumulative ;//累计雨量
	private Double waterLevel ;//水位
	private Double temperature ;//气温
	
	private Double soil_10 ;//10cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_20 ;//20cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_30 ;//30cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_40 ;//40cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_60 ;//60cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_80 ;//80cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_100 ;//100cm处土壤含水量，如果为null值，表示没有监测此值
	private Double soil_upright ;//垂线平均含水量，如果为null值，表示没有监测此值
	

	/**
	 * 分析数据部分
	 */
	public void parseFor_83(ReportDataType206 hp , byte[] b , int index ) throws Exception {
		int n = index ; 
		int count = 0 ;

		UtilProtocol u = new UtilProtocol() ;
		
		if(hp.getYuLiang().intValue() == 1){
			int value = 0 ;
			int r = b[n++] ;
			count++ ;
			value += (u.BCD2Int((byte)((r >> 4) & 0xF))) * 10000 ;
			value += (u.BCD2Int((byte)(r & 0xF))) * 1000 ;
			r = b[n++] ;
			count++ ;
			value += (u.BCD2Int((byte)((r >> 4) & 0xF))) * 100 ;
			value += (u.BCD2Int((byte)(r & 0xF))) * 10 ;
			r = b[n++] ;
			count++ ;
			value += (u.BCD2Int((byte)((r >> 4) & 0xF))) ;
			Double dot = (u.BCD2Int((byte)(r & 0xF)))/10.0 ;
			
			this.rainCumulative = new Double(value) + dot ;
		}
		if(hp.getShuiWei().intValue() == 1){
			//水位，单位为米
			int flag = 1 ;
			int value = 0 ;
			Double dot = 0.0 ;
			int r = b[n++] ;
			count++ ;
			flag = (u.BCD2Int((byte)((r >> 4) & 0xF))) ;
			value += (u.BCD2Int((byte)(r & 0xF))) * 1000 ;
			r = b[n++] ;
			count++ ;
			value += (u.BCD2Int((byte)((r >> 4) & 0xF))) * 100 ;
			value += (u.BCD2Int((byte)(r & 0xF))) * 10 ;
			r = b[n++] ;
			count++ ;
			value += (u.BCD2Int((byte)((r >> 4) & 0xF))) ;
			dot += (u.BCD2Int((byte)(r & 0xF)))/10.0 ;
			r = b[n++] ;
			count++ ;
			dot += (u.BCD2Int((byte)((r >> 4) & 0xF)))/100.0 ;
			dot += (u.BCD2Int((byte)(r & 0xF)))/1000.0 ;
			if(flag == 1){
				this.waterLevel = - (new Double(value) + dot) ;
			}else{
				this.waterLevel = new Double(value) + dot ;
			}
		}
		if(hp.getLiuLiang().intValue() == 1){
			log.error("流量数据本版本未规定。" ) ;
		}
		if(hp.getLiuSu().intValue() == 1){
			log.error("流速数据本版本未规定。" ) ;
		}
		if(hp.getZhaWei().intValue() == 1){
			log.error("闸位数据本版本未规定。" ) ;
		}
		if(hp.getGongLu().intValue() == 1){
			log.error("功率数据本版本未规定。" ) ;
		}
		if(hp.getQiYa().intValue() == 1){
			log.error("气压数据本版本未规定。" ) ;
		}
		if(hp.getFengSu().intValue() == 1){
			log.error("风速数据本版本未规定。" ) ;
		}
		if(hp.getShuiWen().intValue() == 1){
			log.error("水温数据本版本未规定。" ) ;
		}
		if(hp.getShuiZhi().intValue() == 1){
			log.error("水质数据本版本未规定。" ) ;
		}
		if(hp.getTuRang().intValue() == 1){
			//土壤墒情
			int value = 0 ;
			Double dot = 0.0 ;
			int r1 = b[n++] ;
			count++ ;
			int r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_10 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_20 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_30 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_40 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_60 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_80 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_100 = new Double(value) + dot ;
			}
			r1 = b[n++] ;
			count++ ;
			r2 = b[n++] ;
			count++ ;
			if(r1 != 0xFF && r2 != 0xFF){
				value += (u.BCD2Int((byte)(r1 & 0xF))) * 10 ;
				value += (u.BCD2Int((byte)((r2 >> 4) & 0xF))) ;
				dot += (u.BCD2Int((byte)(r2 & 0xF)))/10.0 ;
				this.soil_upright = new Double(value) + dot ;
			}
		} 
		if(hp.getZhengFa().intValue() == 1){
			log.error("蒸发量数据本版本未规定。" ) ;
		}
//		if(hp.getQiWen().intValue() == 1){
//			//气温，单位为摄氏度
//			int flag = 1 ;
//			int value = 0 ;
//			Double dot = 0.0 ;
//			int r = b[n++] ;
//			count++ ;
//			flag = (u.BCD2Int((byte)((r >> 4) & 0xF))) ;
//			value += (u.BCD2Int((byte)(r & 0xF))) * 10 ;
//			r = b[n++] ;
//			count++ ;
//			value += (u.BCD2Int((byte)((r >> 4) & 0xF))) ;
//			dot += (u.BCD2Int((byte)(r & 0xF)))/10.0 ;
//			if(flag == 1){
//				this.temperature = - (new Double(value) + dot) ;
//			}else{
//				this.temperature = new Double(value) + dot ;
//			}
//		}
		
		this.dataBytes = count ;
		
	}
	
	public int getDataBytes() {
		return dataBytes;
	}
	public void setDataBytes(int dataBytes) {
		this.dataBytes = dataBytes;
	}
	public Double getRainCumulative() {
		return rainCumulative;
	}
	public void setRainCumulative(Double rainCumulative) {
		this.rainCumulative = rainCumulative;
	}
	public Double getWaterLevel() {
		return waterLevel;
	}
	public void setWaterLevel(Double waterLevel) {
		this.waterLevel = waterLevel;
	}
	
	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getSoil_10() {
		return soil_10;
	}

	public void setSoil_10(Double soil_10) {
		this.soil_10 = soil_10;
	}

	public Double getSoil_20() {
		return soil_20;
	}

	public void setSoil_20(Double soil_20) {
		this.soil_20 = soil_20;
	}

	public Double getSoil_30() {
		return soil_30;
	}

	public void setSoil_30(Double soil_30) {
		this.soil_30 = soil_30;
	}

	public Double getSoil_40() {
		return soil_40;
	}

	public void setSoil_40(Double soil_40) {
		this.soil_40 = soil_40;
	}

	public Double getSoil_60() {
		return soil_60;
	}

	public void setSoil_60(Double soil_60) {
		this.soil_60 = soil_60;
	}

	public Double getSoil_80() {
		return soil_80;
	}

	public void setSoil_80(Double soil_80) {
		this.soil_80 = soil_80;
	}

	public Double getSoil_100() {
		return soil_100;
	}

	public void setSoil_100(Double soil_100) {
		this.soil_100 = soil_100;
	}

	public Double getSoil_upright() {
		return soil_upright;
	}

	public void setSoil_upright(Double soilUpright) {
		soil_upright = soilUpright;
	}



}
