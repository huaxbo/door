package com.am.cs12.commu.protocol.amRtu206.cdF1;

public class Param206_cdF1 {
	public static final String KEY = Param206_cdF1.class.getName() ;
	
	/*
	 * 00H代表关
	 * 01H代表开
	 * 02H代表停
	 */
	private Integer state ;//门控制

	public String toString(){
		String s = super.toString() ;
		s += "\n门控制：\n" ;
		s += "操作:" + (this.state==null?"未知":this.state) + "\n" ;
		
		return s ;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
