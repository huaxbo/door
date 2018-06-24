package com.am.cs12.commu.core.remoteCommand.forGprsSerial;

import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.ProtocolBaseVO;



/**
 * 队列结点
 * @author liu runyu
 *
 */
public class CommandNodeForGprsSerial {
	
	protected CommandNodeForGprsSerial pre ;
	protected CommandNodeForGprsSerial next ;

	protected CommandTypeForGprsSerial.CommandType commandType ;//命令类型,原生命令或字节数组形式
	protected CommandTypeForGprsSerial.CommandSendPriority commandSendPriority ;//命令优先级
	protected CommandTypeForGprsSerial.CommandSendTimes commandSendTimes ;//命令发送次数
	protected CommandTypeForGprsSerial.CommandSendByNoOnLine commandSendByNoOnLine ;//非在线不发送
	protected String commandId ;//命令ID
	protected String id ;//终端 ID
	protected String code ;//命令的功能码
	protected Object commandData ;//命令
	protected int sendTimes ;//命令发送次数
	private long life ;//命令生命期，从达到最大重发命令次数时计时
	private boolean successed ;//命令成功，即收到命令应答
//	protected boolean hasSetSuccessStatus ;//已经将命令成功在测控终端状态中设置了
	protected boolean canDeal;//可以处理的
	
	private static ProtocolBaseVO pbvo ;
	
	public CommandNodeForGprsSerial(
			CommandTypeForGprsSerial.CommandType commandType ,
			CommandTypeForGprsSerial.CommandSendPriority commandSendPriority ,
			CommandTypeForGprsSerial.CommandSendTimes commandSendTimes ,
			CommandTypeForGprsSerial.CommandSendByNoOnLine commandSendByNoOnLine,
			String commandId , 
			String rtuId , 
			String code ,
			Object commandData) {
		this.commandType = commandType ;
		this.commandSendPriority = commandSendPriority ;
		this.commandSendTimes = commandSendTimes ;
		this.commandSendByNoOnLine = commandSendByNoOnLine ;
		this.commandId = commandId ;
		this.id = rtuId ;
		this.code = code ;
		this.commandData = commandData ;
		this.sendTimes = 0 ;
		this.life = System.currentTimeMillis() ;
		this.successed = false ;
//		this.hasSetSuccessStatus = false ;
		this.canDeal = true ;
		
		if(pbvo == null){
			pbvo = ConfigCenter.instance().getProtocolBaseVO() ;
		}
	}
	
	/**
	 * 命令是否成功，
	 * @return
	 */
	protected boolean isSuccessed(){
		return this.successed ;
	}
	
	/**
	 * 增加发送次数，
	 * 当发送命令次数达到或超过允许的最大次数时，进行命令超时计时
	 */
	protected void addSendTimes(){
		this.sendTimes++ ;
		if(this.sendTimes >= pbvo.maxSendCommandTimes){
			//发送命令次数达到或超过允许的最大次数
			this.life = System.currentTimeMillis() ;
		}
	}
	/**
	 * 发送次数是否有效
	 * @return
	 */
	protected boolean isValidTimes(){
		if(this.commandSendTimes.equals(CommandTypeForGprsSerial.CommandSendTimes.single)){
			//单次发送命令
			if(this.sendTimes >= 1){
				//已经发送过了
				return false ;
			}
		}
		if(this.sendTimes >= pbvo.maxSendCommandTimes){
			//发送命令次数达到或超过允许的最大次数
			return false ;
		}
		return true ;
	}
	/**
	 * 生命周期是否有效
	 * @return
	 */
	protected boolean isValidLife(){
		if(this.sendTimes >= pbvo.maxSendCommandTimes){
			//发送命令次数达到或超过允许的最大次数
			if((System.currentTimeMillis() - this.life) > pbvo.commandTimeout * 1000){
				//命令超时
				return false ;
			}
		}
		return true ;
	}
	
	/**
	 * 延长生命令周期，例如召测图像数据，需要多次上报才会把数据收全，所以要延长命令生命周期
	 */
	protected void lengthenLife(){
		this.life = System.currentTimeMillis() ;
	}
	
	/**
	 * 在考虑发送次数情况下，生命周期是否有效
	 * @return
	 */
	protected boolean isValidLifeWithSendTimes(){
		if(this.sendTimes >= pbvo.maxSendCommandTimes){
			//发送命令次数达到或超过允许的最大次数
			if((System.currentTimeMillis() - this.life) > pbvo.commandTimeout * 1000){
				//命令超时
				return false ;
			}
		}
		return true ;
	}
	
	/**
	 * 匹配命令
	 * @param rtuId RTU ID
	 * @param code 命令功能码
	 * @param success 命令结果回来，是否算是命令已经成功
	 * @return
	 */
	protected String matchCommand(String rtuId , String code , boolean success){
		if(this.id.equals(rtuId) && this.code.equalsIgnoreCase(code)){
			//匹配成功 
			this.successed = success ;
			this.canDeal = true ;
			return this.commandId ;
		}
		return null ;
	}
	
}
