package fr.poc.kafka.person.infrastructure.secondary.kafka;

import fr.poc.kafka.person.domain.ent.Person;

public record PersonMessage(
        String notification,
        Person payload
) {
}
