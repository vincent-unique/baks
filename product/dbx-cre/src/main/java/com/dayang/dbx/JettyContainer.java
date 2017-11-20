package com.dayang.dbx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Vincent on 2017/8/31 0031.
 */

@Primary
@Configuration
@PropertySource("classpath:required/deploy.properties")
public class JettyContainer implements EmbeddedServletContainerCustomizer {

    @Value("${jetty.contextname}")
    private String contextName;

    @Value("${jetty.port}")
    private String port;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setContextPath("/"+contextName);
        container.setPort(Integer.valueOf(port));
    }

}

