package fr.poc.kafka.person.domain.ports.secondary;

import fr.poc.kafka.person.domain.ent.Person;

import java.util.List;
import java.util.Optional;

/**
 * Domain Layer - Port (Secondary/Driven Port)
 */
public interface IPersonRepository {

    List<Person> getPersonList();

    Optional<Person> getById(long id);

    Person saveOrUpdate(Person person);

    boolean deleteById(long id);
}
