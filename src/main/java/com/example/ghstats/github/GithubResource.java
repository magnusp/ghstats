package com.example.ghstats.github;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Component
@RequestMapping("/users")
public interface GithubResource {
    @GetMapping("/{name}")
    Mono<GithubUser> fetchUser(@PathVariable("name") String name);

    @GetMapping("/{name}/repos")
    Mono<List<GithubRepo>> fetchRepos(@PathVariable("name") String name);
}

