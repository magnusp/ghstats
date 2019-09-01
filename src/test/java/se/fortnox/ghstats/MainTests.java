package se.fortnox.ghstats;

import io.fabric8.mockwebserver.DefaultMockServer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import se.fortnox.ghstats.github.GithubRepo;
import se.fortnox.ghstats.github.GithubUser;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"web-client.mapped-resource.GithubResource.base-url=http://localhost:20222", "web-client.default-url=http://localhost:20222"})
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
        try {
            // TODO Expectations should be set up per test
            server.start(20222);
        } catch (RuntimeException ex) {
            // NOOP
        }
    }

    @Test
    public void canRequest() {
        webTestClient.get()
                .uri("/api/users/mock")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectBody(GithubUser.class)
                .value(returnedGithubUser -> Assertions.assertThat(returnedGithubUser.getName()).isNotEmpty());
    }


    @Test
    public void canCall() {
        StepVerifier
                .create(userResource.describe("mock"))
                .expectSubscription()
                .consumeNextWith(githubUser -> Assertions.assertThat(githubUser.getName()).isEqualTo("Test user"))
                .verifyComplete();
    }


}
