package se.fortnox.ghstats;


import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.fortnox.ghstats.github.GithubResource;
import se.fortnox.ghstats.github.GithubUser;

@RestController
public class UserResourceImpl implements UserResource {
    private       GithubResource  githubResource;
    private final Tracer tracer;

    @Autowired
    public UserResourceImpl(GithubResource githubResource, Tracer tracer) {
        this.githubResource = githubResource;
        this.tracer = tracer;
    }

    @Override
    public Mono<GithubUser> describe(String username) {
        return Mono.zip(githubResource.fetchUser(username), githubResource.fetchRepos(username), (githubUser, githubRepos) -> {
            githubRepos.forEach(githubRepo -> {
                githubUser.getRepos().add(githubRepo.getName());
            });
            return githubUser;
        }).flatMap(githubUser -> {
            return Mono.subscriberContext().flatMap(context -> {
                Span span = context.get(Span.class);
                Span userspace = tracer.buildSpan("userspace").asChildOf(span).start();
                userspace.log("Some user data");
                return Mono.just(githubUser).doOnTerminate(userspace::finish);
            });
        });
    }
}