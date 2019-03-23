package com.example.ghstats.application.framework;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

public class InvokingWebClientBeanFactory {
    @Autowired
    InvokingWebClient invokingWebClient;

    @SuppressWarnings("unchecked")
    public <T> T createInvokingWebClient(ClassLoader classLoader, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(classLoader, new Class[] {clazz}, invokingWebClient);
    }
}
