package fr.poc.kafka.person.infrastructure.primary.kafka;

import fr.poc.kafka.person.infrastructure.secondary.kafka.PersonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class PersonConsumer {

    private final PersonOnDemandConsumer personOnDemandConsumer;
    private static final long MAX_TIME_TO_WAIT_POLLING_MS = 1000;

    public PersonConsumer(PersonOnDemandConsumer personOnDemandConsumer) {
        this.personOnDemandConsumer = personOnDemandConsumer;
    }

    public PersonMessage readSingleNextMessage() {
        log.info("Réception du message en cours...");
        List<PersonMessage> messageList = personOnDemandConsumer.pollOnce(Duration.ofMillis(MAX_TIME_TO_WAIT_POLLING_MS));
        log.debug("Polled messages: {}", messageList);
        if (messageList.isEmpty()) {
            return new PersonMessage(String.format("No notification received after %s ms!", MAX_TIME_TO_WAIT_POLLING_MS), null);
        }
        return messageList.getFirst();
    };

//    @KafkaListener(id = PERSON_CONSUMER_GROUP1_ID, topics = PERSON_TOPIC_NAME)
//    public void consuming(String message) {
//        System.out.println("Received message: " + message);
//    }

}