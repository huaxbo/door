package com.automic.door.serv.init;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.am.cs12.Server;

/**
 * Application Lifecycle Listener implementation class Listener1
 *
 */
@WebListener
public class CsInit implements ServletContextListener {

    Logger log = LogManager.getLogger(CsInit.class);
    
    /**
     * Default constructor. 
     */
    public CsInit() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
        //启动通讯服务
        try {
			new Server().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{}
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }
    
}
