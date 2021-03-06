package se.fortnox.ghstats;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import se.fortnox.ghstats.github.GithubRepo;
import se.fortnox.ghstats.github.GithubResource;
import se.fortnox.ghstats.github.GithubUser;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RestController
public class UserResourceImpl implements UserResource {
	private       GithubResource githubResource;
	private final Tracer         tracer;
    private final ConnectionFactory connectionFactory;
    private final SimpleRepo simpleRepo;

	private static <T> Consumer<Signal<T>> logOnNext(Function<T, String> logStatement) {
		return signal -> {
			if (!signal.isOnNext()) return;
			Optional<Span> maybeSpan = signal.getContext().getOrEmpty(Span.class);

			maybeSpan.ifPresent(span -> span.log(logStatement.apply(signal.get())));
		};
	}

	@Autowired
	public UserResourceImpl(GithubResource githubResource, Tracer tracer, ConnectionFactory connectionFactory, SimpleRepo simpleRepo) {
        this.githubResource = githubResource;
        this.tracer = tracer;
        this.connectionFactory = connectionFactory;
        this.simpleRepo = simpleRepo;
    }

	@Override
	public Mono<GithubUser> describe(String username) {
		Mono<GithubUser> userObservable = githubResource
			.fetchUser(username)
			.doOnEach(logOnNext(githubUser -> String.format("Fetched user %s", githubUser.getName())));

		Mono<List<GithubRepo>> repoObservable = githubResource
			.fetchRepos(username)
			.doOnEach(logOnNext(githubRepos -> String.format("Fetched %d repos", githubRepos.size())));

		return Mono.zip(userObservable, repoObservable, (githubUser, githubRepos) -> {
			githubRepos.forEach(githubRepo -> githubUser.getRepos().add(githubRepo.getName()));
			return githubUser;
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