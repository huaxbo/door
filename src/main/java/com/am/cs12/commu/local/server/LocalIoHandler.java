package com.am.cs12.commu.local.server ;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


import com.am.cs12.commu.core.despatchReceive.forLocal.DespatchFromLocal;


public class LocalIoHandler extends IoHandlerAdapter {

    private static Logger LOGGER = LogManager.getLogger(LocalIoHandler.class.getName());

    /**
     * 会话open时回调的方法 
     */
    public void sessionOpened(IoSession session) throws Exception {
    }

    /**
     * 发生异常时回调的方法 
     */
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
//        LOGGER.error(cause.getMessage() , cause);
        LOGGER.error(cause.getMessage());
    }

    /**
     * 接收到数据后，回调的方法，进行数据处理
     */
    public void messageReceived(IoSession session, Object message) throws Exception {
    	new DespatchFromLocal().receiveData(session, message) ;
     }


}
