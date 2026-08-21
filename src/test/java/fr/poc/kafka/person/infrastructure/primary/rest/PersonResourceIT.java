package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.AbstractIntegrationTestsBase;
import fr.poc.kafka.openapi.model.AddressDto;
import fr.poc.kafka.openapi.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonResourceIT extends AbstractIntegrationTestsBase {

    private RestTestClient restClient;
    private RestTestClient restClientUnauthenticated;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        restClientUnauthenticated = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/person")
                .defaultHeader("Content-Type", "application/json")
                .build();

        restClient = restClientUnauthenticated
                // Ajoute l'en-tête Authorization: Basic ...
                .mutate()
                .requestInterceptor(new BasicAuthenticationInterceptor("test-user", "password"))
                .build();

    }

    @Test
    void whenUnauthenticated_thenReturns401() {
        restClientUnauthenticated
                .get()
                .uri("/generate")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized(); // Vérifie que le 401 Unauthorized est bien renvoyé
    }

    @Test
    void whenGetPerson_thenReturnResult() {
        // Tu retrouves ta syntaxe d'origine à 100% !
        PersonDto personActual = restClient
                .get()
                .uri("/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDto.class)
                .value(person -> {
                    assertThat(person).isNotNull();
                    assertThat(person.getId()).isEqualTo(1L);
                    assertThat(person.getFirstname()).isNotEmpty();
                    assertThat(person.getLastname()).isNotEmpty();
                    assertThat(person.getAge()).isGreaterThan(0).isLessThan(100);
                })
                .returnResult()
                .getResponseBody();

        log.info("Person get : {}", personActual);
    }

    @Test
    @Order(Integer.MIN_VALUE)
        //on lit le csv chargé !
    void whenListPerson_thenAllReturned() {
        // Tu retrouves ta syntaxe d'origine à 100% !
        List<PersonDto> personsActual = restClient
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<PersonDto>>() {
                })
                .value(persons -> {
                    assertThat(persons).isNotNull();
                    assertThat(persons.size()).isEqualTo(5);
                })
                .returnResult()
                .getResponseBody();

        log.info("Person listed all : {}", personsActual);
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
                    assertThat(person.getAddress().getCity()).isNotEmpty();
                    assertThat(person.getAddress().getStreet()).isNotEmpty();
                    assertThat(person.getAddress().getZipcode()).isNotNull();
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

        AddressDto addressDto = new AddressDto().city("Lyon").street("115 rue du bourbonnais").zipcode(69009L);

        PersonDto personDtoExpected = new PersonDto();
        personDtoExpected.setAge(age);
        personDtoExpected.setFirstname(firstname);
        personDtoExpected.setLastname(lastname);
        personDtoExpected.setAddress(addressDto);
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
                .jsonPath("$.age").isEqualTo(age)
                .jsonPath("$.address.street").isEqualTo(addressDto.getStreet())
                .jsonPath("$.address.city").isEqualTo(addressDto.getCity())
                .jsonPath("$.address.zipcode").isEqualTo(addressDto.getZipcode());
        // .jsonPath("$.creationDate").isEqualTo(LocalDate.of(2026, 4, 28).format(DateTimeFormatter.ISO_DATE));
    }

}
