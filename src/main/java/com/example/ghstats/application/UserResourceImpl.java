package com.example.ghstats.application;


import com.example.ghstats.framework.InvokingWebClient;
import com.example.ghstats.github.GithubResource;
import com.example.ghstats.github.GithubUser;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserResourceImpl implements UserResource {
    /*private final GithubResource githubResource;

    @Autowired
    public UserResourceImpl(GithubResource githubResource) {
        this.githubResource = githubResource;
    }*/

    @Override
    public Mono<GithubUser> index() {
        var iwc = new InvokingWebClient<>();
        final GithubResource githubResource = iwc.of(GithubResource.class);
        final Flux<GithubUser> slask          = githubResource.request("ignored");
        int                    i              = 0;
        return slask.single();
    }
}
