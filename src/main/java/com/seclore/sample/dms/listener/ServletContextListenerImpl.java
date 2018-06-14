package com.seclore.sample.dms.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.seclore.sample.dms.config.SecloreWSClientConfig;
import com.seclore.sample.dms.util.Global;

/**
 * Application Lifecycle Listener implementation class ServletContextListenerImpl
 *
 * @author harindra.chaudhary
 */
@WebListener public class ServletContextListenerImpl implements ServletContextListener {
    /**
     * Default constructor.
     */
    public ServletContextListenerImpl() {

    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
        String appPath = sce.getServletContext().getRealPath("");
        System.out.println("App Path is " + appPath);

        // Initialize application specific requirements
        Global.initializeApplication(appPath);


        // ========= Seclore WSClient Initialization  START =========//
        SecloreWSClientConfig.initializeWSClient(appPath);
        // ========= Seclore WSClient Initialization  END =========//
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {

        // =========== Terminate Seclore WS Client START =========//
        SecloreWSClientConfig.terminateWSClient();
        // =========== Terminate Seclore WS Client END =========//
    }

}
