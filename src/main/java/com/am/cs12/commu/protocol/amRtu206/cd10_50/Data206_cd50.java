package com.am.cs12.commu.protocol.amRtu206.cd10_50;


public class Data206_cd50 {
	
	private String rtuId ;

	public String toString(){
		String s = "\n" ;
		s += "rtuId" + ":" + (this.rtuId==null?"":this.rtuId) ;
		return s ;
	}

	public String getRtuId() {
		return rtuId;
	}

	public void setRtuId(String rtuId) {
		this.rtuId = rtuId;
	}


}
