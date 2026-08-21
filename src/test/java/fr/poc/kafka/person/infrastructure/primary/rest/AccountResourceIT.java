package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.AbstractIntegrationTestsBase;
import fr.poc.kafka.openapi.model.AccountDto;
import fr.poc.kafka.openapi.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountResourceIT extends AbstractIntegrationTestsBase {

    @Autowired
    private PasswordEncoder bcryptEncoder;
    private RestTestClient restClient;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestTestClient restClientUnauthenticated = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/account")
                .defaultHeader("Content-Type", "application/json")
                .build();

        restClient = restClientUnauthenticated
                // Ajoute l'en-tête Authorization: Basic ...
                .mutate()
                .requestInterceptor(new BasicAuthenticationInterceptor("test-user", "password"))
                .build();

    }

    @Test
    void whenCreateAccount_thenCreatedAndResultsReturned() {
        String login = "ffo_login";
        String password = "ffo_password";
        long idPerson = 1L;

        AccountDto accountDto = new AccountDto().id(null).login(login).password(password).personId(1L).person(null);

        restClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder.path("/createOrUpdate")
                                .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").value(id -> assertThat((Integer) id)
                        .isGreaterThan(0))
                .jsonPath("$.login").isEqualTo(login)
                .jsonPath("$.password").value((String returnedHash) -> {
                    assertThat(bcryptEncoder.matches(password, returnedHash)).isTrue();
                })
                .jsonPath("$.personId").isEqualTo(idPerson)
                .jsonPath("$.person").isEmpty();

    }

    @Test
    void whenGetAccount_thenReturnResult() {
        // Tu retrouves ta syntaxe d'origine à 100% !
        AccountDto accountActual = restClient
                .get()
                .uri("/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountDto.class)
                .value(account -> {
                    assertThat(account).isNotNull();
                    assertThat(account.getId()).isEqualTo(1L);
                    assertThat(account.getLogin()).isEqualTo("jdupont");
                    assertThat(account.getPassword()).isEqualTo("jdupont_passw");
                })
                .returnResult()
                .getResponseBody();

        log.info("Account get : {}", accountActual);
    }

    @Test
    @Order(Integer.MIN_VALUE)
        //on lit le csv chargé !
    void whenListAccount_thenAllReturned() {
        // Tu retrouves ta syntaxe d'origine à 100% !
        List<AccountDto> accountsActual = restClient
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<AccountDto>>() {
                })
                .value(accounts -> {
                    assertThat(accounts).isNotNull();
                    assertThat(accounts.size()).isEqualTo(2);
                    assertThat(accounts.getFirst().getLogin()).isEqualTo("jdupont");
                    assertThat(accounts.get(1).getLogin()).isEqualTo("smartin");
                })
                .returnResult()
                .getResponseBody();

        log.info("Accounts listed all : {}", accountsActual);
    }
}
