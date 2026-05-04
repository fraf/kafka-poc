package fr.poc.kafka.person.infrastructure.mappers;

import fr.poc.kafka.person.domain.vo.Address;
import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.infrastructure.primary.dtos.PersonDto;
import fr.poc.kafka.person.infrastructure.secondary.entities.PersonEntity;
import org.springframework.stereotype.Component;

@Component

public class PersonMapper {

    public Person mapToPerson(PersonDto personDto) {
        return new Person(personDto.personId(), personDto.firstname(), personDto.lastname(), personDto.age(), new Address("Rue1", "", null));
    }

    public Person mapToPerson(PersonEntity personEntity) {
        return new Person(personEntity.id, personEntity.firstname, personEntity.lastname, personEntity.age, new Address("Rue2", "", null));
    }

    public PersonDto mapToPersonDto(Person person) {
        return new PersonDto(person.id(), person.firstname(), person.lastname(), person.age());
    }

    public PersonEntity mapToPersonEntity(Person person) {
        return PersonEntity.builder().id(person.id()).age(person.age()).firstname(person.firstname()).lastname(person.lastname()).build();
    }
}
