package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.openapi.model.PersonDto;
import fr.poc.kafka.openapi.rest.PersonApi;
import fr.poc.kafka.person.application.PersonApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonResource implements PersonApi {

    private final PersonApplicationService personApplicationService;


    public PersonResource(PersonApplicationService personApplicationService) {
        this.personApplicationService = personApplicationService;
    }

    public ResponseEntity<PersonDto> getPerson(Long id) {
        PersonDto personDto = personApplicationService.getPersonDto(id);
        return new ResponseEntity<>(personDto, personDto == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    public ResponseEntity<PersonDto> createOrUpdatePerson(PersonDto personneSk) {
        return new ResponseEntity<>(personApplicationService.createOrUpdatePersonDto(personneSk), HttpStatus.CREATED);
    }
}