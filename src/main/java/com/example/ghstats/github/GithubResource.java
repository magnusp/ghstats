package com.example.ghstats.github;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Component
@RequestMapping("/users")
public interface GithubResource {
    @GetMapping("/{name}")
    Flux<GithubUser> request(@PathVariable String name);
}

