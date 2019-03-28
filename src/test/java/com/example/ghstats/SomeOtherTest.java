package com.example.ghstats;

import com.example.ghstats.application.framework.InvokingWebClient;
import com.example.ghstats.application.framework.InvokingWebClientBeanFactory;
import com.example.ghstats.application.framework.WebClientConfig;
import com.example.ghstats.github.GithubUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class SomeOtherTest {
    @Test
    public void canDoStuff() {
        var webClientMock = mock(WebClient.class);
        var requestSpec = mock(WebClient.RequestBodyUriSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClientMock.method(any(HttpMethod.class))).thenReturn(requestSpec);
        when(requestSpec.uri(anyString())).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(eq(GithubUser.class))).thenReturn(Flux.just(new GithubUser()));

        var webClientConfig = new WebClientConfig();
        webClientConfig.setBaseUrl("http://example.com");
        var factory = new InvokingWebClientBeanFactory(webClientConfig);

        var userResource = factory.createInvokingWebClient(factory.getClass().getClassLoader(), TestResource.class);
        final Flux<Boolean> result = userResource.resourceA();
        result.subscribe();
    }

    @RequestMapping("/root")
    interface TestResource {
        @GetMapping("/a")
        Flux<Boolean> resourceA();
    }
}
