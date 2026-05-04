package fr.poc.kafka.person.domain.ports.secondary;

import fr.poc.kafka.person.domain.ent.Person;

/**
 * Domain Layer - Port (Secondary/Driven Port)
 */
public interface IPersonEventDispatcher {

    void notifyCreated(Person person);
}
