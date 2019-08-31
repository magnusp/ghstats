package se.fortnox.ghstats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import se.fortnox.ghstats.github.GithubUser;

@RequestMapping("/api")
public interface UserResource {
    @GetMapping("/users/{username}")
    Mono<GithubUser> describe(@PathVariable("username") String username);
}
