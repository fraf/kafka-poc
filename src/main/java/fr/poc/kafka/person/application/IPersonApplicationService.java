package fr.poc.kafka.person.application;

import fr.poc.kafka.openapi.model.PersonDto;

import java.util.List;

public interface IPersonApplicationService {

    PersonDto getPersonDto(long id);

    List<PersonDto> getPersonDtoList();

    PersonDto createOrUpdatePersonDto(PersonDto personDto);
}
