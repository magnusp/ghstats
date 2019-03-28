package com.example.ghstats.application.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvokingWebClientConfig {
    @Autowired
    WebClientConfig webClientConfig;

    @Bean
    public InvokingWebClient invokingWebClient() {
        var webClientConfig = new WebClientConfig();
        return new InvokingWebClient(webClientConfig);
    }

    @Bean(name="invokingWebClientBeanFactory")
    public InvokingWebClientBeanFactory invokingWebClientBeanFactory() {
        return new InvokingWebClientBeanFactory(webClientConfig);
    }



}
