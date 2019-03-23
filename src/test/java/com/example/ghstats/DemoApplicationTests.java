package com.example.ghstats;

import com.example.ghstats.application.DemoApplication;
import com.example.ghstats.application.UserResource;
import com.example.ghstats.github.GithubUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {DemoApplication.class})
public class DemoApplicationTests {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    UserResource userResource;

    @Test
    public void canRequest() {
        webTestClient.get()
                .uri("/api/idx")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectBody(GithubUser.class)
        .value(githubUser -> Assertions.assertThat(githubUser.getName()).isNotEmpty());
    }

    @Test
    public void canCall() {
        StepVerifier
                .create(userResource.index())
                .expectSubscription()
                .consumeNextWith(githubUser -> Assertions.assertThat(githubUser.getName()).isNotEmpty())
                .verifyComplete();
    }

}
