package com.am.cs12.commu.protocol.amRtu206.cd11_51;


public class Param206_cd11 {

	public static final String KEY = Param206_cd11.class.getName() ;
	
	private String clock ;

	public String toString(){
		String s = "\n" ;
		s += "新时钟" + ":" + (this.clock==null?"":this.clock) ;
		return s ;
	}

	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}

}
