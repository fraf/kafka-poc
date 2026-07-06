package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.AbstractIntegrationTestsBase;
import fr.poc.kafka.openapi.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class PersonResourceIT extends AbstractIntegrationTestsBase {

    private RestTestClient restClient;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        restClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/person")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Test
    void whenGetPerson_thenNotFound() {
        // Tu retrouves ta syntaxe d'origine à 100% !
        PersonDto personActual = restClient
                .get()
                .uri("/generate")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDto.class)
                .value(person -> {
                    assertThat(person).isNotNull();
                    assertThat(person.getId()).isEqualTo(0L);
                    assertThat(person.getFirstname()).isNotEmpty();
                    assertThat(person.getLastname()).isNotEmpty();
                    assertThat(person.getAge()).isGreaterThan(0).isLessThan(100);
                })
                .returnResult()
                .getResponseBody();

        log.info("Person generated : {}", personActual);
    }

    @Test
    void whenGeneratePerson_thenAllFieldsFilled() {

        PersonDto personActual = restClient
                .get()
                .uri("/generate")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDto.class)
                .value(person -> {
                    assertThat(person).isNotNull();
                    assertThat(person.getId()).isEqualTo(0L);
                    assertThat(person.getFirstname()).isNotEmpty();
                    assertThat(person.getLastname()).isNotEmpty();
                    assertThat(person.getAge()).isGreaterThan(0).isLessThan(100);
                })
                .returnResult()
                .getResponseBody();

        log.info("Person generated : {}", personActual);
    }

    @Test
    void whenCreatePerson_thenCreatedAndResultsReturned() {
        String firstname = "François";
        String lastname = "FOURNEL";
        int age = 45;

        PersonDto personDtoExpected = new PersonDto();
        personDtoExpected.setAge(age);
        personDtoExpected.setFirstname(firstname);
        personDtoExpected.setLastname(lastname);
        restClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder.path("/createOrUpdate")
                                .queryParam("sendNotification", Boolean.FALSE)
                                .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(personDtoExpected)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").value(id -> assertThat((Integer) id)
                        .isGreaterThan(0))
                .jsonPath("$.firstname").isEqualTo(firstname)
                .jsonPath("$.lastname").isEqualTo(lastname)
                .jsonPath("$.age").isEqualTo(age);
        // .jsonPath("$.creationDate").isEqualTo(LocalDate.of(2026, 4, 28).format(DateTimeFormatter.ISO_DATE));
    }

}
