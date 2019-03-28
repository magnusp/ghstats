package com.example.ghstats.application;

import com.example.ghstats.github.GithubRepo;
import com.example.ghstats.github.GithubUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/api")
public interface UserResource {
    @GetMapping("/users/{username}")
    Mono<GithubUser> describe(@PathVariable("username") String username);
}
