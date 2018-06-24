package com.am.cs12.commu.core.despatchReceive.forLocal;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import com.am.cs12.command.Command;
import com.am.cs12.command.CommandType;
import com.am.cs12.commu.protocol.ProtocolParam;

public class DespatchFromSupplyCallPicCommand {
	
	private static Logger log = LogManager.getLogger(DespatchFromSupplyCallPicCommand.class.getName()) ;
	/**
	 * 接收补召图像命令
	 * @param data 原生命令
	 */
	public void receiveSupplyCommand(Object data){
		Command com = (Command)data ;
		String cmmmandId = com.getId() ;
		if(cmmmandId == null){
			log.error("出错，收到补召图像数据命令ID为空！") ;
			return ;
		}		
		HashMap<String, Object> params = com.getParams() ;
		if(params == null || params.size() == 0){
			log.error("出错，收到补召图像数据命令参数集合为空！") ;
			return ;
		}
		ProtocolParam pp = (ProtocolParam)params.get(ProtocolParam.KEY) ;
		String id = pp.getId() ;
		String code = pp.getCode() ;

		if(id == null || id.equals("")){
			log.error("出错，收到补召图像数据命令参数集合中无RTU ID！") ;
			return ;
		}
		if(code == null || code.equals("")){
			log.error("出错，收到补召图像数据命令参数集合中无功能码！") ;
			return ;
		}

		String commandType = com.getType() ;
		if(commandType == null){
			log.error("出错，收到补召图像数据命令类型为空！") ;
			return ;
		}else if(commandType.equals(CommandType.innerCommand)){
			//通信服务器内部命令，例如查询通信服务器时钟，查询在测控器在线线情况等
			log.error("出错，该命令不应该是通信服务器内部命令！") ;
			return ;
		}else if(commandType.equals(CommandType.outerCommand)){
			//针对RTU的命令
			new DealCommandForGprsSerial().dealCommand(null, true, com, id, code) ;
		}
	}

}
