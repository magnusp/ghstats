package com.example.ghstats;

import io.fabric8.mockwebserver.DefaultMockServer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import se.fortnox.ghstats.Main;
import se.fortnox.ghstats.UserResource;
import se.fortnox.ghstats.github.GithubRepo;
import se.fortnox.ghstats.github.GithubUser;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Main.class})
public class MainTests {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    UserResource userResource;

    @Before
    public void setup() {
        GithubRepo githubRepo = new GithubRepo();
        githubRepo.setName("hej");
        GithubUser githubUser = new GithubUser("Test user");
        DefaultMockServer server = new DefaultMockServer();
        server
                .expect()
                .get()
                .withPath("/users/mock")
                .andReturn(200, githubUser)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .always();
        server
                .expect()
                .get()
                .withPath("/users/mock/repos")
                .andReturn(200, Arrays.asList(githubRepo))
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .always();
    }

    @Test
    @Ignore("unable to set host and port from DefaultMockServer on WebClientConfig during setup")
    public void canRequest() {
        webTestClient.get()
                .uri("/api/users/mock")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectBody(GithubUser.class)
                .value(returnedGithubUser -> Assertions.assertThat(returnedGithubUser.getName()).isNotEmpty());
    }


    @Test
    @Ignore("unable to set host and port from DefaultMockServer on WebClientConfig during setup")
    public void canCall() {
        StepVerifier
                .create(userResource.describe("mock"))
                .expectSubscription()
                .consumeNextWith(githubUser -> Assertions.assertThat(githubUser.getName()).isEqualTo("Test user"))
                .verifyComplete();
    }


}
