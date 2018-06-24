package com.am.cs12.commu.core.remoteCommand.forSM;

import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;

public class DealCachCommandForSM {
	
	private static DealCachCommandForSM instance ;
	

	private DealCachCommandForSM(){
	}
	/**
	 * 得到单实例
	 * @return
	 */
	public static DealCachCommandForSM instance(){
		if(instance == null){
			instance = new DealCachCommandForSM() ;
		}
		return instance ;
	}
	
	/**
	 * 加入等待发送的命令
	 * @param rtuId
	 * @param data
	 */
	public void setCommand(
			String commandId ,
			String rtuId, 
			String code)throws Exception{
		if(commandId == null){
			throw new Exception("设置已经通过短信通道发送的命令时，必须提供命令ID！") ;
		}
		if(rtuId == null){
			throw new Exception("设置已经通过短信通道发送的命令时，必须提供RTU ID！") ;
		}
		if(code == null){
			throw new Exception("设置已经通过短信通道发送的命令时，必须提供命令功能码！") ;
		}
		CommandQueueForSM queue = RemoteSessionManager.instance().getCachCommandQueueForSM(rtuId) ;
		synchronized(queue){
			CommandNodeForSM node = new CommandNodeForSM(
					commandId,
					rtuId, 
					code, 
					System.currentTimeMillis()) ;
			queue.addTail(node) ;
			queue.notifyAll() ;
		}
	}
	
	
	/**
	 * 当命令成功，收到命令结果时，要从命令缓存中匹配命令
	 * @param dutId
	 * @param code
	 * @return 返回命令ID
	 */
	public String successCommand(String rtuId , String code){
		String commandId = null ;
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		CommandQueueForSM queue = rsm.getCachCommandQueueForSM(rtuId) ;
		synchronized(queue){
			int comNum = queue.size() ;
			if(comNum > 0){
				for(int i = 0 ; i < comNum ; i++){
					CommandNodeForSM node = queue.get(i) ;
					commandId = node.matchCommand(rtuId, code) ;
					if(commandId != null){
						break ;
					}
				}
			}
			queue.notifyAll() ;
		}
		return commandId ;
	}
	

}
