package name.genese.salathiel.issuespringwebfluxreactiveerroradvice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToApplicationContext;
import static reactor.core.publisher.Mono.just;

@SpringBootTest(classes = IssueSpringWebfluxReactiveErrorAdviceApplication.class)
class IssueSpringWebfluxReactiveErrorAdviceApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webTestClient;

    @BeforeEach
    public void beforeEach() {
        webTestClient = bindToApplicationContext(applicationContext).build();
    }

    @Test
    void returnOkWhenPostWithValidAccount() {
        final String name = "Hope";

        webTestClient.post().uri(AccountController.PATH_PREFIX)
                .body(just(new Account(name)), Account.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.content").isEqualTo(format("Willkommen, %s!", name));
    }

    @Test
    void returnUnprocessableEntityWhenPostWithInvalidAccount() {
        webTestClient.post().uri(AccountController.PATH_PREFIX)
                .body(just(new Account()), Account.class)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().valueEquals(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    }

}
