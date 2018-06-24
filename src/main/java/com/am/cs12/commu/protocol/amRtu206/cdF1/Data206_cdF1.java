package com.am.cs12.commu.protocol.amRtu206.cdF1;


public class Data206_cdF1 {
	
	private String state;

	public String toString(){
		String s = "门控制操作:" + (state==null?"":state);
		return s ;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	
}
