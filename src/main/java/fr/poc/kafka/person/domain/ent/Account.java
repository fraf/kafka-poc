package fr.poc.kafka.person.domain.ent;

import lombok.With;

@With
public record Account(
        Long id,
        String login,
        String password,
        Long personId,
        Person person
) {
}
