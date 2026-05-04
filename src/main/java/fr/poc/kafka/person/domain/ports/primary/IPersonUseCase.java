package fr.poc.kafka.person.domain.ports.primary;

import fr.poc.kafka.person.domain.ent.Person;

import java.util.List;
import java.util.Optional;

/**
 * Domain Layer - Port (Primary/Driving Port). What my domain can do/offer ?
 */
public interface IPersonUseCase {

    Optional<Person> getPerson(long id);

    List<Person> getPersonList();

    Person createOrUpdatePerson(Person person);


    boolean deletePerson(long id);
}
