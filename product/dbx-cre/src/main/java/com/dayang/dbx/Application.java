package com.dayang.dbx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Vincent on 2017/8/31 0031.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Application.class, args);
    }

    /**
     * Default container
     * @return
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer(){
        JettyEmbeddedServletContainerFactory jettyFactory = new JettyEmbeddedServletContainerFactory();
//        jettyFactory.setContextPath("/dbx");
//        jettyFactory.setPort(8086);
        return jettyFactory;
    }
}
