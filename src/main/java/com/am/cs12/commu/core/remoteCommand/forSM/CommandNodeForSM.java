package com.am.cs12.commu.core.remoteCommand.forSM;


public class CommandNodeForSM {
	
	protected CommandNodeForSM pre ;
	protected CommandNodeForSM next ;

	public String commandId ;
	public String rtuId ;
	public String code ;
	public Long sendTime ;

	
	public CommandNodeForSM(String commandId, String rtuId , String code , Long sendTime){
		this.commandId = commandId ;
		this.rtuId = rtuId ;
		this.code = code ;
		this.sendTime = sendTime ;
	}
	/**
	 * 匹配命令
	 * @param code
	 * @return
	 */
	protected String matchCommand(String rtuId , String code){
		if(this.rtuId.equals(rtuId) && this.code.equalsIgnoreCase(code)){
			//匹配成功，表示命令成功
			return this.commandId ;
		}
		return null ;
	}
}
