package fr.poc.kafka.person.infrastructure.mappers;

import fr.poc.kafka.openapi.model.PersonDto;
import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.infrastructure.secondary.entities.PersonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person mapToPerson(PersonDto personDto);

    Person mapToPerson(PersonEntity personEntity);

    PersonDto mapToPersonDto(Person person);

    PersonEntity mapToPersonEntity(Person person);
}
