package com.am.cs12.commu.protocol.amLocal;

public class Param {
	
	public static final String KEY = Param.class.getName() ;

	private String[] ids ;//查询部分测控器运行数据时，所提供的测控器  ID 

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

}
