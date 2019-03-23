package com.example.ghstats.application.framework;

import com.example.ghstats.github.GithubUser;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;

public class InvokingWebClient<T> implements InvocationHandler {
    private WebClient webClient;

    public InvokingWebClient() {

    }

    public InvokingWebClient(URL baseUrl) {
        this(WebClient.builder().baseUrl(baseUrl.toExternalForm()).build());
    }

    public InvokingWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<T> invoke(Object o, Method method, Object[] arguments) throws Throwable {
        if (arguments == null) {
            arguments = new Object[0];
        }
        return (Flux<T>) webClient
                .method(HttpMethod.GET)
                .uri("/users/magnusp")
                .retrieve()
                .bodyToFlux(GithubUser.class);
    }
}