/**
 * 
 */
package com.automic.door.util.cmder;

import java.util.HashMap;


/**
 * @author at
 * 处理召测命令返回结果
 */
public class CmdRlt {

	private static HashMap<String,Object[]> result = new HashMap<String,Object[]>(0);
	private int defaultWaitTime = 30;//等待命令结果时间（s）
	
	/**
	 * 
	 */
	private CmdRlt(){}
	
	/**
	 * single instance
	 * @return
	 */
	public static CmdRlt singleInstance(){
		CmdRlt scr = new CmdRlt();
		
		return scr; 
	}
	
	/**
	 * 获取命令结果
	 * @param commandId
	 * @return
	 */
	private Object getCmdRlt(String commandId,long wt){
		Object[] oo = new Object[]{null};
		if(!result.containsKey(commandId)){
			result.put(commandId, oo);
		}else{
			oo = result.get(commandId);
		}
		synchronized(oo){
			try {
				oo.wait(wt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				oo = result.remove(commandId);
			}
		}
		
		return oo[0];
	}
	
	/**
	 * 
	 * @param cmdId
	 * @param waitTime(单位：s)
	 * @return
	 */
	public Object getCmdRltWait(String cmdId,Integer waitTime){
		long wt = defaultWaitTime * 1000;
		
		if(waitTime != null){
			wt = waitTime *1000;
		}
		
		return getCmdRlt(cmdId,wt);
	}
	
	/**
	 * 放置命令结果
	 * @param cmdId
	 * @param dp
	 */
	public void putCmdRlt(String cmdId,Object dp){
		Object[] oo = null;
		if(result.containsKey(cmdId)){
			oo = result.get(cmdId);
			synchronized(oo){
				oo[0] = dp;
				oo.notifyAll();
			}
		}
	}
	
	/**
	 * 生成cmdid
	 * @return
	 */
	public String generateCmdId(){
		synchronized(this){
			
			return System.currentTimeMillis() + "";
		}		
	}
}
