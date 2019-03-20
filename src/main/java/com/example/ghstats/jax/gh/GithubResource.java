package com.example.ghstats.jax.gh;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Service
@Component
@RequestMapping("/users")
public interface GithubResource {
    @GetMapping("/{name}")
    Mono<GithubUser> getData(@PathVariable String name);
}

