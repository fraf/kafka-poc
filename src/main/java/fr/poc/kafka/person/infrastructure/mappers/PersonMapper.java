package fr.poc.kafka.person.infrastructure.mappers;

import fr.poc.kafka.openapi.model.PersonDto;
import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.infrastructure.secondary.entities.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person mapToPerson(PersonDto personDto);

    @Mapping(source = "street", target = "address.street")
    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "zipcode", target = "address.zipcode")
    Person mapToPerson(PersonEntity personEntity);

    PersonDto mapToPersonDto(Person person);

    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.zipcode", target = "zipcode")
    PersonEntity mapToPersonEntity(Person person);
}
