package fr.poc.kafka.person.infrastructure.primary.rest;


import fr.poc.kafka.AbstractIntegrationTestsBase;
import fr.poc.kafka.openapi.model.AddressDto;
import fr.poc.kafka.openapi.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.AutoConfigureRestClient;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.web.servlet.client.RestTestClient;

@Slf4j
@AutoConfigureRestClient
public class NotificationResourceIT extends AbstractIntegrationTestsBase {

    private RestTestClient restClientUnauthenticated;
    private RestTestClient restClient;
    @LocalServerPort
    private int port;

    @Autowired
    private PersonResource personResource;

    @Autowired
    private NotificationResource notificationResource;

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
    void whenCreatePerson_thenCreatedAndNotificationAvailable() {
        String firstname = "François_WithNotif";
        String lastname = "FOURNEL_WithNotif";
        int age = 45;

        AddressDto addressDto = new AddressDto().city("Lyon").street("115 rue du bourbonnais").zipcode(69009L);

        PersonDto personDtoExpected = new PersonDto();
        personDtoExpected.setAge(age);
        personDtoExpected.setFirstname(firstname);
        personDtoExpected.setLastname(lastname);
        personDtoExpected.setAddress(addressDto);

        //GIVEN
        restClient = RestTestClient.bindToController(personResource)
                .baseUrl("/person")
                .defaultHeader("ContentType", "application/json")
                .build();

        restClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder.path("/createOrUpdate")
                                .queryParam("sendNotification", Boolean.TRUE)
                                .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(personDtoExpected)
                .exchange()
                .expectStatus()
                .isCreated();

        //WHEN
        restClient = RestTestClient.bindToController(notificationResource)
                .baseUrl("/notification")
                .defaultHeader("ContentType", "application/json")
                .build();

        //THEN
        restClient
                .get()
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.notification").isEqualTo("Created:")
                .jsonPath("$.personne").isNotEmpty()
                .jsonPath("$.personne").isEqualTo(personDtoExpected);
    }

}
