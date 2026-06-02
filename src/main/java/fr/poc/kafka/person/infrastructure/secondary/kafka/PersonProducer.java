package fr.poc.kafka.person.infrastructure.secondary.kafka;

import fr.poc.kafka.config.KafkaConfiguration;
import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.domain.ports.secondary.IPersonEventDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonProducer implements IPersonEventDispatcher {

    private final KafkaTemplate<String, PersonMessage> kafkaTemplate;

    public PersonProducer(KafkaTemplate<String, PersonMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void notifyCreated(Person person) {
        PersonMessage personMessage = new PersonMessage("Created:", person);
        log.info("Envoi du message en cours : {}", personMessage);
        kafkaTemplate
                .send(KafkaConfiguration.PERSON_TOPIC_NAME, personMessage)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message envoyé avec succès : message={}, offset={}"
                            ,result
                            ,result.getRecordMetadata().offset());
                    } else {
                        log.error("Erreur lors de l'envoi du message", ex);
                    }
                });
    }
}
