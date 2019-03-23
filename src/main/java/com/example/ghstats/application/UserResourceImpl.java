package com.example.ghstats.application;


import com.example.ghstats.github.GithubResource;
import com.example.ghstats.github.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import static reactor.core.publisher.Mono.just;

@RestController
public class UserResourceImpl implements UserResource {
    private GithubResource githubResource;

    @Autowired
    public UserResourceImpl(GithubResource githubResource) {
        this.githubResource = githubResource;
    }

    @Override
    public Mono<GithubUser> index() {
        /*
        Response time in seconds should be 1 + (# of concurrent request * outgoing request latency)
         */
        return Mono.delay(Duration.ofSeconds(1)).flatMap(aLong -> {
            return githubResource.request("foo").single();
        });
    }
}
