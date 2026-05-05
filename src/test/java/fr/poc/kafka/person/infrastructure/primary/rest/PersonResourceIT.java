package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.AbstractIntegrationTestsBase;
import fr.poc.kafka.openapi.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.AutoConfigureRestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@Slf4j
@AutoConfigureRestClient
class PersonResourceIT extends AbstractIntegrationTestsBase {


    private RestTestClient restClient;

    @Autowired
    private PersonResource personResource;

    @BeforeEach
    public void setup() {
        restClient = RestTestClient.bindToController(personResource)
                .baseUrl("/person")
                .defaultHeader("ContentType", "application/json")
                .build();
    }

    @Test
    void whenGetPerson_thenNotFound() {
        restClient
                .get()
                .uri("/0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
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
                .uri("/createOrUpdate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(personDtoExpected)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").value(_ -> new GreaterThan<>(1))
                .jsonPath("$.firstname").isEqualTo(firstname)
                .jsonPath("$.lastname").isEqualTo(lastname)
                .jsonPath("$.age").isEqualTo(age);
        // .jsonPath("$.creationDate").isEqualTo(LocalDate.of(2026, 4, 28).format(DateTimeFormatter.ISO_DATE));
    }

}
