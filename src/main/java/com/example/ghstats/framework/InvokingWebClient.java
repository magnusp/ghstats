package com.example.ghstats.framework;

import com.example.ghstats.github.GithubUser;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.newProxyInstance;

public class InvokingWebClient<T> implements InvocationHandler {
	public <T> T of(Class<T> resourceClass) {
		return (T)newProxyInstance(resourceClass.getClassLoader(), new Class[]{resourceClass}, this);
	}

	@Override
	public Flux<T> invoke(Object o, Method method, Object[] arguments) throws Throwable {
		if (arguments == null) {
			arguments = new Object[0];
		}
		final WebClient build = WebClient.builder().baseUrl("https://api.github.com").build();
		return (Flux<T>) build
			.method(HttpMethod.GET)
			.uri("/users/magnusp")
			.retrieve()
			.bodyToFlux(GithubUser.class);

		//return (Flux<T>) Mono.just(new GithubUser("Alice")).flux();

	}
}
