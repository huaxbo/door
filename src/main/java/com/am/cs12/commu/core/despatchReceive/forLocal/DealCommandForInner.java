package com.am.cs12.commu.core.despatchReceive.forLocal;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.mina.core.session.IoSession;

import com.am.cs12.command.Command;
import com.am.cs12.commu.core.innerCommand.InnerCommandDealer;

public class DealCommandForInner {
	private static Logger log = LogManager.getLogger(DealCommandForInner.class.getName()) ;
	/**
	 * 处理内部命令
	 * @param localSession
	 * @param com
	 */
	protected void dealInnerCommand(IoSession localSession, Command com){
		try {
			new InnerCommandDealer().deal(localSession, com) ;
		} catch (Exception e) {
			log.error("处理通信服务器本地命令出错，" + e.getMessage() , e) ;
		}
	}
	

}
