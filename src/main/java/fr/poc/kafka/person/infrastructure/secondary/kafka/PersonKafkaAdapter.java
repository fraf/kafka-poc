package fr.poc.kafka.person.infrastructure.secondary.kafka;

import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.domain.ports.secondary.IPersonEventDispatcher;
import org.springframework.stereotype.Component;

@Component
public class PersonKafkaAdapter implements IPersonEventDispatcher {
    @Override
    public void notifyCreated(Person person) {

    }
}
