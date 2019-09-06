package se.fortnox.ghstats;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
public class DbConfig extends AbstractR2dbcConfiguration {
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionConfiguration build = PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)
                .username("postgres")
                .password("mysecretpassword")
                .build();
        return new PostgresqlConnectionFactory(build);
    }
}
