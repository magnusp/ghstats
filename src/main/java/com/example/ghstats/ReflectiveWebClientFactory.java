package com.example.ghstats;

import com.example.ghstats.jax.gh.GithubUser;
import reactor.core.publisher.Mono;

import java.lang.reflect.Proxy;

public class ReflectiveWebClientFactory<T> {
    public T construct(Class<? extends T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
           return Mono.just(new GithubUser("Alice"));
        });
    }
}
