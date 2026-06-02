package fr.poc.kafka.person.infrastructure.primary.rest;


import fr.poc.kafka.AbstractIntegrationTestsBase;
import fr.poc.kafka.openapi.model.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.AutoConfigureRestClient;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@AutoConfigureRestClient
public class NotificationResourceIT extends AbstractIntegrationTestsBase {

    @Container
    @ServiceConnection
    static protected PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:18.3"));

    //    https://www.conduktor.io/blog/testing-kafka-testcontainers-embedded-mocks#:~:text=EmbeddedKafka%20vs%20Testcontainers,-Feature&text=Use%20%40EmbeddedKafka%20for%20simple%20Spring,unit%20tests%20catch%20most%20bugs.
    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        // On récupère l'URL dynamique du conteneur (ex: localhost:49152)
        // et on la donne à Spring avant que les Beans ne se lancent
        //@ServiceConnection ne fonctionne pas
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    private RestTestClient restClient;

    @Autowired
    private PersonResource personResource;

    @Autowired
    private NotificationResource notificationResource;


    @Test
    void whenCreatePerson_thenCreatedAndNotificationAvailable() {
        String firstname = "François_WithNotif";
        String lastname = "FOURNEL_WithNotif";
        int age = 45;

        PersonDto personDtoExpected = new PersonDto();
        personDtoExpected.setAge(age);
        personDtoExpected.setFirstname(firstname);
        personDtoExpected.setLastname(lastname);

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
