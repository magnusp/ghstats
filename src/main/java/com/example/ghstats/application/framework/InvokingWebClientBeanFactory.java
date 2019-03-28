package com.example.ghstats.application.framework;

import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Proxy;

public class InvokingWebClientBeanFactory {
    private WebClientConfig webClientConfig;

    @Autowired
    public InvokingWebClientBeanFactory(WebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }

    @SuppressWarnings("unchecked")
    public <T> T createInvokingWebClient(ClassLoader classLoader, Class<T> clazz) {
        var invokingWebClient = new InvokingWebClient(webClientConfig);
        return (T) Proxy.newProxyInstance(classLoader, new Class[] {clazz}, invokingWebClient);
    }
}
