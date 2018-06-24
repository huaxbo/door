package com.am.cs12.commu.protocol.amRtu206.cd12_52;


public class Param206_cd12 {

	public static final String KEY = Param206_cd12.class.getName() ;

	/*
	工作模式类型=00B，设置遥测终端在兼容工作状态；
	工作模式类型=01B，设置遥测终端在自报工作状态；
	工作模式类型=02B，设置遥测终端在查询/应答工作状态；
	工作模式类型=03B，遥测终端在调试/维修状态。
	*/
	
	public static final int modelCompatible = 0 ;//工作模式类型=00B，设置遥测终端在兼容工作状态；
	public static final int modelAutoReport = 1 ;//工作模式类型=01B，设置遥测终端在自报工作状态；
	public static final int modelReadAnswer = 2 ;//工作模式类型=02B，设置遥测终端在查询/应答工作状态；
	public static final int modelDebug = 3 ;//工作模式类型=03B，遥测终端在调试/维修状态。
	

	private Integer model_0to2 ;

	public String toString(){
		String s = "\n" ;
		s += "model" + ":" + (this.model_0to2==null?"":
			this.model_0to2.intValue()==0?"兼容工作状态":
				this.model_0to2.intValue()==1?"自报工作状态":
					this.model_0to2.intValue()==2?"查询/应答工作状态":
						this.model_0to2.intValue()==3?"调试/维修状态":
							"出错，未知的RTU工作模式"
			) ;
		return s ;
	}

	public Integer getModel_0to2() {
		return model_0to2;
	}

	public void setModel_0to2(Integer model_0to2) {
		this.model_0to2 = model_0to2;
	}

}
