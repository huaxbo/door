package com.am.cs12.commu.protocol.amRtu206.cdF1;

import com.am.cs12.commu.protocol.amRtu206.cdF4.Data206_cdF4;


public class Data206_cdF1 extends Data206_cdF4{
	
	private String state;

	public String toString(){
		String s = "门控制操作:" + (state==null?"":state);
		return s + super.toString();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	
}
