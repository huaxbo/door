package com.am.cs12.commu.protocol.amRtu206.cd10_50;


public class Param206_cd10 {

	public static final String KEY = Param206_cd10.class.getName() ;
	
	private String newId ;

	public String toString(){
		String s = "\n" ;
		s += "newId" + ":" + (this.newId==null?"":this.newId) ;
		return s ;
	}

	public String getNewId() {
		return newId;
	}

	public void setNewId(String newId) {
		this.newId = newId;
	}


}
