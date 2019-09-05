package se.fortnox.ghstats;


import io.opentracing.Span;
import io.opentracing.Tracer;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.fortnox.ghstats.github.GithubResource;
import se.fortnox.ghstats.github.GithubUser;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class UserResourceImpl implements UserResource {
    private       GithubResource  githubResource;
    private final Tracer tracer;
    private final ConnectionFactory connectionFactory;
    private final SimpleRepo simpleRepo;

    @Autowired
    public UserResourceImpl(GithubResource githubResource, Tracer tracer, ConnectionFactory connectionFactory, SimpleRepo simpleRepo) {
        this.githubResource = githubResource;
        this.tracer = tracer;
        this.connectionFactory = connectionFactory;
        this.simpleRepo = simpleRepo;
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
                Span span = context.getOrDefault(Span.class, null);
                if(span == null) {
                    return Mono.just(githubUser);
                }
                Span userspace = tracer.buildSpan("userspace").asChildOf(span).start();
                userspace.log("Some user data");
                return Mono.just(githubUser).doOnTerminate(userspace::finish);
            });
        });
    }

    @Override
    public Mono<String> doStuff() {
        return simpleRepo.doQuery().map(simple -> {
            return simple.getValue();
        });
        /*return DatabaseClient.create(connectionFactory)
                .execute()
                .sql("select 'hello' as c1;")
                .fetch()
                .first()
                .map(stringObjectMap -> (String) stringObjectMap.get("c1"));*/
    }
    @Override
    public Flux<GithubUser> stream() {
        List<Mono<GithubUser>> fluxes = List.of(
                githubResource.fetchUser("magnusp"),
                githubResource.fetchUser("magnusp").delaySubscription(Duration.of(5, ChronoUnit.SECONDS))
        );
        return Flux.concat(fluxes);
    }
}