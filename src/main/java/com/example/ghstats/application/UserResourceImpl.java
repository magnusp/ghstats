package com.example.ghstats.application;


import com.example.ghstats.application.framework.WebClientConfig;
import com.example.ghstats.github.GithubRepo;
import com.example.ghstats.github.GithubResource;
import com.example.ghstats.github.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class UserResourceImpl implements UserResource {
    private GithubResource githubResource;
    private final WebClientConfig webClientConfig;

    @Autowired
    public UserResourceImpl(GithubResource githubResource, WebClientConfig webClientConfig) {
        this.githubResource = githubResource;
        this.webClientConfig = webClientConfig;
    }

    @Override
    public Mono<GithubUser> describe(String username) {
        return Mono.zip(githubResource.fetchUser(username), githubResource.fetchRepos(username), (githubUser, githubRepos) -> {
            githubRepos.forEach(githubRepo -> {
                githubUser.getRepos().add(githubRepo.getName());
            });
            return githubUser;
        });
    }
}
