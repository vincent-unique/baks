package com.dayang.dbx.application.listener;


import com.dayang.dbx.model.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.trump.vincent.utilities.CloseUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * Created by Vincent on 2017/9/3 0003.
 */
@WebListener
public class ApplicationInit extends ContextLoaderListener {
    private static Logger logger = LoggerFactory.getLogger(ApplicationInit.class);

    public static WebApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){

        super.contextInitialized(servletContextEvent);
        ServletContext servletContext = servletContextEvent.getServletContext();

        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        logger.info("The Application[ "+applicationContext.getApplicationName()+" ] Is Starting up.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){

        if(ConnectionManager.source!=null){
            CloseUtil.close(ConnectionManager.source.getConnection());
        }
        if(ConnectionManager.target!=null){
            CloseUtil.close(ConnectionManager.target.getConnection());
        }
        logger.info("The Application[ "+applicationContext.getApplicationName()+" ] Is Closed.");
        super.contextDestroyed(servletContextEvent);

    }

}
