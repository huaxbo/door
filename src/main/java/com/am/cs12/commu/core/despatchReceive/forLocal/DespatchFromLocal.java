package com.am.cs12.commu.core.despatchReceive.forLocal ;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import com.am.cs12.command.* ;
import com.am.cs12.commu.protocol.*;
import com.am.cs12.config.*;
import com.am.cs12.ServerConfig; 
import com.am.cs12.config.MeterHelp;

public class DespatchFromLocal {
	
	private static Logger log = LogManager.getLogger(DespatchFromLocal.class.getName()) ;
	/**
	 * 接收到本地业务系统命令数据
	 * @param localSession 与业务系统的网络连接会话
	 * @param data 命令
	 * @param knowRemoteOnLine 是否知道远程测控器应该在线（如果是常在线的方式，必然知道应该是在线，如果存在低功耗测控器，那么就不能知道在线情况）
	 */
	public void receiveData(IoSession localSession, Object data){
		
		if(data == null){
			new ReturnMessage().errored(localSession , "出错，收到业务系统数据为空！") ;
			return ;
		}
		
		MinaData mina = (MinaData)data ;
		Command com = mina.getCom() ;
		if(com == null){
			new ReturnMessage().errored(localSession , "出错，收到业务系统传来的命令为空！") ;
			return ;
		}
		try {
			log.info("收到业务系统数据：\n" + com.toXml()) ;
		} catch (Exception e) {
		}
		
		String cmmmandId = com.getId() ;
		if(cmmmandId == null){
			new ReturnMessage().errored(localSession , "出错，收到业务系统传来的命令ID为空！") ;
			return ;
		}		
		String commandType = com.getType() ;
		if(commandType == null){
			new ReturnMessage().errored(localSession , "出错，收到业务系统传来的命令类型为空！") ;
			return ;
		}else if(commandType.equals(CommandType.innerCommand)){
			//通信服务器内部命令，例如查询通信服务器时钟，查询测控器在线线情况等
			new DealCommandForInner().dealInnerCommand(localSession, com) ;
		}else{
			this.channelSelect(localSession, com, commandType) ;
		}
	}
	
	/**
	 * 进行GPRS通道及SM通道选择
	 * @param localSession
	 * @param com
	 * @param knowRemoteOnLine
	 * @param commandType
	 */
	private void channelSelect(IoSession localSession, Command com, String commandType){
		HashMap<String, Object> params = com.getParams() ;
		if(params == null || params.size() == 0){
			new ReturnMessage().errored(localSession , "出错，收到业务系统传来的命令参数集合为空！") ;
			return ;
		}
		ProtocolParam pp = (ProtocolParam)params.get(ProtocolParam.KEY) ;
		String id = pp.getId() ;
		String code = pp.getCode() ;

		if(id == null || id.equals("")){
			new ReturnMessage().errored(localSession , "出错，收到业务系统传来的命令参数集合中无 ID！") ;
			return ;
		}
		if(code == null || code.equals("")){
			new ReturnMessage().errored(localSession , "出错，收到业务系统传来的命令参数集合中无功能码！") ;
			return ;
		}
		
		boolean selectedSm = false ;
		SmServerVO smVO = ConfigCenter.instance().getSmServerVO() ;
		if(smVO != null){
			//首先判断是否为只由短信发送的命令
			for(int i = 0 ; i < smVO.codeOnlyBySm.length ; i++){
				if(code.equals(smVO.codeOnlyBySm[i])){
					//只能由短信通道发送的命令
					this.selectedSM(localSession, com, commandType, id, code) ;
					selectedSm = true ;
					break ;
				}
			}
		}
		if(!selectedSm){
			if(ServerConfig.commandSendType.equals(ServerConfig.gprsStat)){
				//系统只支持 GPRS或卫星通道发送
				this.selectedGprsStat(localSession, com, commandType, id, code) ;
			}else if(ServerConfig.commandSendType.equals(ServerConfig.sm)){
				//系统只支持 短信通道发送
				this.selectedSM(localSession, com, commandType, id, code) ;
			}else if(ServerConfig.commandSendType.equals(ServerConfig.gprsStat_sm)){
				//系统支持  ( GPRS或卫星通道)和(短信通道)，但GPRS或卫星通道优先
				if(new MeterHelp().getMeterOnlineAlways(id)){
					//终端是常在线方式
					this.selectedGprsStat(localSession, com, commandType, id , code) ;
				}else{
					//终端是非常在线方式
					for(int i = 0 ; i < smVO.codeEnableBySm.length ; i++){
						if(code.equals(smVO.codeEnableBySm[i])){
							//短信通道所能支持的命令
							this.selectedSM(localSession, com, commandType, id, code) ;
							selectedSm = true ;
							break ;
						}
					}
					if(!selectedSm){
						//短信通道不能支持的命令，还是由GPRS或卫星通道发送
						this.selectedGprsStat(localSession, com, commandType, id , code) ;
					}
				}
			}else if(ServerConfig.commandSendType.equals(ServerConfig.sm_gprsStat)){
				//系统支持  (短信通道)和(GPRS或卫星通道)，但短信通道优先
				for(int i = 0 ; i < smVO.codeEnableBySm.length ; i++){
					if(code.equals(smVO.codeEnableBySm[i])){
						//短信通道所能支持的命令
						this.selectedSM(localSession, com, commandType, id, code) ;
						selectedSm = true ;
						break ;
					}
				}
				if(!selectedSm){
					//短信通道不能支持的命令，还是由GPRS或卫星通道发送
					this.selectedGprsStat(localSession, com, commandType, id , code) ;
				}
			}
		}
		
	}
	
	/**
	 * 选择GPRS或卫星通道发送命令
	 * @param localSession
	 * @param com
	 * @param rtuId
	 * @param code
	 */
	private void selectedGprsStat(IoSession localSession, Command com, String commandType, String id , String code){
		if(commandType.equals(CommandType.outerCommand)){
			//针对RTU的命令
			new DealCommandForGprsSerial().dealCommand(localSession,new MeterHelp().getMeterOnlineAlways(id), com, id , code) ;
		}
	}
	/**
	 * 选择SM短信通道发送命令
	 * @param localSession
	 * @param com
	 * @param id
	 * @param code
	 */
	private void selectedSM(IoSession localSession, Command com, String commandType, String id , String code){
		if(commandType.equals(CommandType.outerCommand)){
			//针对RTU的命令
			new DealCommandForSM().dealCommand(localSession, com, id , code) ;
		}
	}

}
