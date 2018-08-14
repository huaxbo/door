package com.automic.door.util.cmder;

import java.util.Hashtable;


import com.am.cs12.commu.protocol.Data;

public class CmdRltCache {
	
	private Hashtable<String,Object[]> rltCache = new Hashtable<String,Object[]>(0);
	private int vali = 1000;//单位：毫秒
	private static CmdRltCache singleObj;
	
	private CmdRltCache(){}
	
	public static CmdRltCache singleInstance(){
		if(singleObj == null){
			
			singleObj = new CmdRltCache();
		}
		
		return singleObj;
	}
	
	/**
	 * @param dtuId
	 * @param d
	 */
	public synchronized void pushRlt(String dtuId,Data d){
		rltCache.put(dtuId, new Object[]{d,System.currentTimeMillis()});
	}
	
	/**
	 * @param dtuId
	 * @return
	 */
	public synchronized Data getRlt(String dtuId){
		Object[] oo = rltCache.get(dtuId);
		if(oo == null){
			
			return null;
		}
		long curr = System.currentTimeMillis();
		
		if(curr - (long)oo[1] > vali){
			
			return null;
		}
		
		return (Data)oo[0];
	}
	

}
