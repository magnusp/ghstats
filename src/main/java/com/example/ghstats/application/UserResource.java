package com.example.ghstats.application;

import com.example.ghstats.github.GithubUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/api")
public interface UserResource {
    @GetMapping("/idx")
    Mono<GithubUser> index();
}
