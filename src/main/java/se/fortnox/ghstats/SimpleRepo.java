package se.fortnox.ghstats;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SimpleRepo extends ReactiveCrudRepository<Simple, Long> {
    @Query("SELECT 'hello' AS value")
    Mono<Simple> doQuery();
}
