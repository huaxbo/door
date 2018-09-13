package com.automic.door.util.cmder;

import java.util.Hashtable;



public class CmdPwdCache {
	
	private Hashtable<String,String> pwdCache = new Hashtable<String,String>(0);
	private static CmdPwdCache singleObj;
	
	private CmdPwdCache(){}
	
	public static CmdPwdCache singleInstance(){
		if(singleObj == null){
			
			singleObj = new CmdPwdCache();
		}
		
		return singleObj;
	}
	
	/**
	 * @param dtuId
	 * @param pwd
	 */
	public synchronized void pushPwd(String dtuId,String pwd){
		String local = pwdCache.get(dtuId);
		if(local == null || !local.equals(pwd)){
			pwdCache.put(dtuId, pwd);
		}
	}
	
	/**
	 * @param dtuId
	 * @return
	 */
	public synchronized String getPwd(String dtuId){
		
		return pwdCache.get(dtuId);		
	}
	

}
