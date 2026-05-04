package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.person.application.PersonApplicationService;
import fr.poc.kafka.person.infrastructure.primary.dtos.PersonDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/person")
@RestController
public class PersonResource {

    private final PersonApplicationService personApplicationService;


    public PersonResource(PersonApplicationService personApplicationService) {
        this.personApplicationService = personApplicationService;
    }

    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PersonDto> getPerson(@PathVariable("personId") Long personId) {
        PersonDto personDto = personApplicationService.getPersonDto(personId);
        return new ResponseEntity<>(personDto, personDto == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/createOrUpdate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PersonDto> createOrUpdatePerson(@Valid @RequestBody PersonDto personDto) {
        return new ResponseEntity<>(personApplicationService.createOrUpdatePersonDto(personDto), HttpStatus.CREATED);
    }
}