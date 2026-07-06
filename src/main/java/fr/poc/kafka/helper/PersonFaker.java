package fr.poc.kafka.helper;

import fr.poc.kafka.openapi.model.PersonDto;
import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.domain.vo.Address;
import fr.poc.kafka.person.infrastructure.mappers.PersonMapper;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class PersonFaker {

    private final static Faker faker = new Faker(Locale.FRANCE);
    private final PersonMapper personMapper;

    public PersonFaker(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    public Person generate() {
        return new Person(0L, firstName(), lastName(), age(), address());
    }

    public PersonDto generateDto() {
        return personMapper.mapToPersonDto(generate());
    }

    private String firstName() {
        return faker.name().firstName();
    }

    private String lastName() {
        return faker.name().lastName();
    }

    private int age() {
        return faker.number().numberBetween(1, 100);
    }

    private Address address() {
        return new Address(faker.address().streetAddress(), faker.address().cityName(), Integer.valueOf(faker.address().zipCode()));
    }
}
