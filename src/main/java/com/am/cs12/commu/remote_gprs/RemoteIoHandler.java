package com.am.cs12.commu.remote_gprs;

import org.apache.mina.core.service.IoHandlerAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


import com.am.cs12.commu.core.despatchReceive.forRemote.*; 
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;

public class RemoteIoHandler extends IoHandlerAdapter {


	private static Logger LOGGER = LogManager.getLogger(RemoteIoHandler.class.getName());

    /**
     * 会话open时回调的方法 
     */
    public void sessionOpened(IoSession session) throws Exception {
    }

    /**
     * 发生异常时回调的方法 
     */
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
    }
    @Override
	public void sessionClosed(IoSession session) throws Exception {
    	String rtuId = RemoteSessionManager.instance().removeSession(session) ;
    	if(rtuId != null){
    		MeterStatus ms = MeterStatusManager.id_status.get(rtuId);
    		ms.onLining = false;
    	}
		super.sessionClosed(session);
		LOGGER.info("@@@测控设备[" + (rtuId == null ? "" : rtuId) + "]连接关闭！");
	}

    /**
     * 接收到数据后，回调的方法，进行数据处理
     */
    public void messageReceived(IoSession session, Object message) throws Exception {

    	byte[] data = (byte[])message ;
    	
    	DespatchForGprs d = new DespatchForGprs() ;
    	d.receiveData(session, data) ;
  }


}
