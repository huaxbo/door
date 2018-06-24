package com.am.cs12.commu.protocol.amRtu206.cd11_51;


public class Data206_cd51 {
	
	private String clock ;

	public String toString(){
		String s = "\n" ;
		s += "时钟" + ":" + (this.clock==null?"":this.clock) ;
		return s ;
	}

	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}



}
