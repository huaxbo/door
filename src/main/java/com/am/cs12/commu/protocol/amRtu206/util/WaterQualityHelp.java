package com.am.cs12.commu.protocol.amRtu206.util;

public class WaterQualityHelp {

	private int index;
	private int maxLen;
	private int floatLen;
	private String wqnm;
	//单位
	private String dw;
	//公式
	private Double gs;
	
	public WaterQualityHelp(int index,int maxLen,int floatLen,String wqnm,String dw){
		this.floatLen = floatLen;
		this.index = index;
		this.maxLen = maxLen;
		this.wqnm = wqnm;
		this.dw = dw;
		this.gs = Long.valueOf("999999999999".substring(0, maxLen))/Double.valueOf("1000000000".substring(0, floatLen+1));
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getMaxLen() {
		return maxLen;
	}
	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}
	public int getFloatLen() {
		return floatLen;
	}
	public void setFloatLen(int floatLen) {
		this.floatLen = floatLen;
	}
	public String getWqnm() {
		return wqnm;
	}
	public void setWqnm(String wqnm) {
		this.wqnm = wqnm;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public Double getGs() {
		return gs;
	}
	public void setGs(Double gs) {
		this.gs = gs;
	}
	
	
}
