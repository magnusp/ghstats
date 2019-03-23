package com.example.ghstats.application.framework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class InvokingWebClientConfig {
    @Bean
    public InvokingWebClient invokingWebClient() {
        return new InvokingWebClient(WebClient.builder().baseUrl("https://api.github.com").build());
    }

    @Bean(name="invokingWebClientBeanFactory")
    public InvokingWebClientBeanFactory invokingWebClientBeanFactory() {
        return new InvokingWebClientBeanFactory();
    }



}
