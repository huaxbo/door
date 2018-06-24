package com.am.cs12.commu.protocol.amRtu206.common.data;

public class Data206_alt {
	///////////////////////////////////////
	//报警状态
	protected Integer power220Stop_alt ;//工作交流电停电告警；(0未发生报警，1发生报警)
	protected Integer storePowerLowVoltage_alt ;//蓄电池电压报警；(0未发生报警，1发生报警)
	protected Integer waterLevel_alt ;//水位上下限报警；(0未发生报警，1发生报警)
	protected Integer waterFlow_alt ;//流量上下限报警； (0未发生报警，1发生报警)
	protected Integer waterQuality_alt ;//水质上下限报警； (0未发生报警，1发生报警)
	protected Integer waterFlowMeter_alt ;//流量仪表报警； (0未发生报警，1发生报警)
	protected Integer pumpStartStop_alt ;//水泵开停自报；(0未发生报警，1发生报警)
	protected Integer waterLevelMeter_alt ;//水位仪表故障报警； (0未发生报警，1发生报警)
	protected Integer waterYa_alt ;//水压超限报警
	protected Integer ic_alt ;//IC卡功能报警
	protected Integer dingZhi_alt ;//定值控制报警
	protected Integer surplusWater_alt ;//剩余水量下线报警
	protected Integer boxDoorOpen_alt ;//终端箱门状态报警；(0未发生报警，1发生报警)
	
//	protected Integer boardBatteryLowVoltage_alt ;//主板电池电压报警；(0未发生报警，1发生报警)
//	protected Integer instanceRain_alt ;//瞬时雨量报警；(0未发生报警，1发生报警)
//	protected Integer rainMeterCommu_alt ;//雨量仪表通讯故障报警； (0未发生报警，1发生报警)
//	protected Integer waterTemperature_alt ;//水温上下限报警； (0未发生报警，1发生报警)
//	protected Integer waterTemperatureMeter_alt ;//水温仪表报警； (0未发生报警，1发生报警)
//	protected Integer waterQualityMeter_alt ;//水质仪表报警；(0未发生报警，1发生报警) 
//	protected Integer soilWater_alt ;//土壤含水率上下限报警；(0未发生报警，1发生报警) 
//	protected Integer soilWaterMeter_alt ;//土壤含水率仪表报警；(0未发生报警，1发生报警) 
//	protected Integer evaporation_alt ;//蒸发量上下限报警；(0未发生报警，1发生报警)
//	protected Integer evaporationMeter_alt ;//蒸发量仪表报警；(0未发生报警，1发生报警)
//	protected Integer temperature_alt ;//气温上下限报警；(0未发生报警，1发生报警)

	public String toString(){
		String s = "\n报警状态：\n" ;
		s += "power220Stop_alt" + ":" + (this.power220Stop_alt==null?"":this.power220Stop_alt.intValue()) + "\n" ;
		s += "storePowerLowVoltage_alt" + ":" + (this.storePowerLowVoltage_alt==null?"":this.storePowerLowVoltage_alt.intValue()) + "\n" ;
		s += "pumpStartStop_alt" + ":" + (this.pumpStartStop_alt==null?"":this.pumpStartStop_alt.intValue()) + "\n" ;
//		s += "boardBatteryLowVoltage_alt" + ":" + (this.boardBatteryLowVoltage_alt==null?"":this.boardBatteryLowVoltage_alt.intValue()) + "\n" ; 
		s += "boxDoorOpen_alt" + ":" + (this.boxDoorOpen_alt==null?"":this.boxDoorOpen_alt.intValue()) + "\n" ;
//		s += "instanceRain_alt" + ":" + (this.instanceRain_alt==null?"":this.instanceRain_alt.intValue()) + "\n" ;
//		s += "rainMeterCommu_alt" + ":" + (this.rainMeterCommu_alt==null?"":this.rainMeterCommu_alt.intValue()) + "\n" ; 
		s += "waterLevel_alt" + ":" + (this.waterLevel_alt==null?"":this.waterLevel_alt.intValue()) + "\n" ; 
		s += "waterLevelMeter_alt" + ":" + (this.waterLevelMeter_alt==null?"":this.waterLevelMeter_alt.intValue()) + "\n" ;
		s += "waterFlow_alt" + ":" + (this.waterFlow_alt==null?"":this.waterFlow_alt.intValue()) + "\n" ;
		s += "waterFlowMeter_alt" + ":" + (this.waterFlowMeter_alt==null?"":this.waterFlowMeter_alt.intValue()) + "\n" ; 
//		s += "waterTemperature_alt" + ":" + (this.waterTemperature_alt==null?"":this.waterTemperature_alt.intValue()) + "\n" ;
//		s += "waterTemperatureMeter_alt" + ":" + (this.waterTemperatureMeter_alt==null?"":this.waterTemperatureMeter_alt.intValue()) + "\n" ;
		s += "waterQuality_alt" + ":" + (this.waterQuality_alt==null?"":this.waterQuality_alt.intValue()) + "\n" ;
//		s += "waterQualityMeter_alt" + ":" + (this.waterQualityMeter_alt==null?"":this.waterQualityMeter_alt.intValue()) + "\n" ;
//		s += "soilWater_alt" + ":" + (this.soilWater_alt==null?"":this.soilWater_alt.intValue()) + "\n" ; 
//		s += "soilWaterMeter_alt" + ":" + (this.soilWaterMeter_alt==null?"":this.evaporation_alt.intValue()) + "\n" ;
//		s += "evaporation_alt" + ":" + (this.evaporation_alt==null?"":this.evaporation_alt.intValue()) + "\n" ;
//		s += "evaporationMeter_alt" + ":" + (this.evaporationMeter_alt==null?"":this.evaporationMeter_alt.intValue()) + "\n" ;
//		s += "temperature_alt" + ":" + (this.temperature_alt==null?"":this.temperature_alt.intValue()) + "\n" ;

		s += "waterYa_alt" + ":" + (this.waterYa_alt==null?"":this.waterYa_alt.intValue()) + "\n" ;
		s += "ic_alt" + ":" + (this.ic_alt==null?"":this.ic_alt.intValue()) + "\n" ;
		s += "dingZhi_alt" + ":" + (this.dingZhi_alt==null?"":this.dingZhi_alt.intValue()) + "\n" ;
		s += "surplusWater_alt" + ":" + (this.surplusWater_alt==null?"":this.surplusWater_alt.intValue()) + "\n" ;
		return s ;
	}

	public Integer getPower220Stop_alt() {
		return power220Stop_alt;
	}
	public void setPower220Stop_alt(Integer power220StopAlt) {
		power220Stop_alt = power220StopAlt;
	}
	public Integer getStorePowerLowVoltage_alt() {
		return storePowerLowVoltage_alt;
	}
	public void setStorePowerLowVoltage_alt(Integer storePowerLowVoltageAlt) {
		storePowerLowVoltage_alt = storePowerLowVoltageAlt;
	}
	public Integer getPumpStartStop_alt() {
		return pumpStartStop_alt;
	}
	public void setPumpStartStop_alt(Integer pumpStartStopAlt) {
		pumpStartStop_alt = pumpStartStopAlt;
	}
	public Integer getBoxDoorOpen_alt() {
		return boxDoorOpen_alt;
	}
	public void setBoxDoorOpen_alt(Integer boxDoorOpenAlt) {
		boxDoorOpen_alt = boxDoorOpenAlt;
	}
	public Integer getWaterLevel_alt() {
		return waterLevel_alt;
	}
	public void setWaterLevel_alt(Integer waterLevelAlt) {
		waterLevel_alt = waterLevelAlt;
	}
	public Integer getWaterLevelMeter_alt() {
		return waterLevelMeter_alt;
	}
	public void setWaterLevelMeter_alt(Integer waterLevelMeterAlt) {
		waterLevelMeter_alt = waterLevelMeterAlt;
	}
	public Integer getWaterFlow_alt() {
		return waterFlow_alt;
	}
	public void setWaterFlow_alt(Integer waterFlowAlt) {
		waterFlow_alt = waterFlowAlt;
	}
	public Integer getWaterFlowMeter_alt() {
		return waterFlowMeter_alt;
	}
	public void setWaterFlowMeter_alt(Integer waterFlowMeterAlt) {
		waterFlowMeter_alt = waterFlowMeterAlt;
	}

	public Integer getWaterQuality_alt() {
		return waterQuality_alt;
	}

	public void setWaterQuality_alt(Integer waterQualityAlt) {
		waterQuality_alt = waterQualityAlt;
	}

	public Integer getWaterYa_alt() {
		return waterYa_alt;
	}

	public void setWaterYa_alt(Integer waterYaAlt) {
		waterYa_alt = waterYaAlt;
	}

	public Integer getIc_alt() {
		return ic_alt;
	}

	public void setIc_alt(Integer icAlt) {
		ic_alt = icAlt;
	}

	public Integer getDingZhi_alt() {
		return dingZhi_alt;
	}

	public void setDingZhi_alt(Integer dingZhiAlt) {
		dingZhi_alt = dingZhiAlt;
	}

	public Integer getSurplusWater_alt() {
		return surplusWater_alt;
	}

	public void setSurplusWater_alt(Integer surplusWaterAlt) {
		surplusWater_alt = surplusWaterAlt;
	}

}
