package fr.poc.kafka.person.domain.ent;

import fr.poc.kafka.person.domain.vo.Address;

/**
 * Domain Layer - Entity
 */
public record Person(
        Long id,
        String firstname,
        String lastname,
        int age,
        Address address
) {
}
