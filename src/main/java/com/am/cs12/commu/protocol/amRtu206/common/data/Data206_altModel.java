package com.am.cs12.commu.protocol.amRtu206.common.data;

public class Data206_altModel extends Data206_alt {
	/*
	工作模式类型=00B，设置遥测终端在兼容工作状态；
	工作模式类型=01B，设置遥测终端在自报工作状态；
	工作模式类型=02B，设置遥测终端在查询/应答工作状态；
	*/
	
	protected Boolean modelCompatible ;//工作模式类型=00B，设置遥测终端在兼容工作状态；
	protected Boolean modelAutoReport ;//工作模式类型=01B，设置遥测终端在自报工作状态；
	protected Boolean modelReadAnswer ;//工作模式类型=02B，设置遥测终端在查询/应答工作状态；

	public String toString(){
		String s = super.toString() ;
		s += "\n工作模式：" ;
		s += modelCompatible==null?"":(this.modelCompatible.booleanValue()?"兼容工作状态":"") ;
		s += modelAutoReport==null?"":(this.modelAutoReport.booleanValue()?"自报工作状态":"") ;
		s += modelReadAnswer==null?"":(this.modelReadAnswer.booleanValue()?"查询/应答工作状态":"") ;
		return s ;
	}
	public Boolean getModelCompatible() {
		return modelCompatible;
	}
	public void setModelCompatible(Boolean modelCompatible) {
		this.modelCompatible = modelCompatible;
	}
	public Boolean getModelAutoReport() {
		return modelAutoReport;
	}
	public void setModelAutoReport(Boolean modelAutoReport) {
		this.modelAutoReport = modelAutoReport;
	}
	public Boolean getModelReadAnswer() {
		return modelReadAnswer;
	}
	public void setModelReadAnswer(Boolean modelReadAnswer) {
		this.modelReadAnswer = modelReadAnswer;
	}

}
