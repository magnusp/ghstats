package com.example.ghstats.jax;


import com.example.ghstats.ReflectiveWebClientFactory;
import com.example.ghstats.jax.gh.GithubResource;
import com.example.ghstats.jax.gh.GithubUser;
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
        var f = new ReflectiveWebClientFactory<GithubResource>();
        var i = f.construct(GithubResource.class);

        return i.getData("foo");

    }
}
