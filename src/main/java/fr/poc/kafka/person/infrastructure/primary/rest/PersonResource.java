package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.helper.PersonFaker;
import fr.poc.kafka.openapi.model.PersonDto;
import fr.poc.kafka.openapi.rest.PersonApi;
import fr.poc.kafka.person.application.PersonApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonResource implements PersonApi {

    private final PersonApplicationService personApplicationService;
    private final PersonFaker personFaker;


    public PersonResource(PersonApplicationService personApplicationService, PersonFaker personFaker) {
        this.personApplicationService = personApplicationService;
        this.personFaker = personFaker;
    }

    public ResponseEntity<PersonDto> getPerson(Long id) {
        PersonDto personDto = personApplicationService.getPersonDto(id);
        return new ResponseEntity<>(personDto, personDto == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PersonDto>> getPersons() {
        //TODO
        return null;
    }

    public ResponseEntity<PersonDto> createOrUpdatePerson(PersonDto personneSk, Boolean sendNotification) {
        return new ResponseEntity<>(personApplicationService.createOrUpdatePersonDto(personneSk, sendNotification != null && sendNotification), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PersonDto> generatePerson() {
        return new ResponseEntity<>(personFaker.generateDto(), HttpStatus.OK);
    }
}