package fr.poc.kafka.person.domain;

import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.domain.ports.primary.IPersonUseCase;
import fr.poc.kafka.person.domain.ports.secondary.IPersonEventDispatcher;
import fr.poc.kafka.person.domain.ports.secondary.IPersonRepository;

import java.util.List;
import java.util.Optional;

public class PersonService implements IPersonUseCase {

    private final IPersonRepository personRepository;
    private final IPersonEventDispatcher personEventDispatcher;

    public PersonService(IPersonRepository personRepository, IPersonEventDispatcher personEventDispatcher) {
        this.personRepository = personRepository;
        this.personEventDispatcher = personEventDispatcher;
    }

    @Override
    public Optional<Person> getPerson(long id) {
        return personRepository.getById(id);
    }

    @Override
    public List<Person> getPersonList() {
        return personRepository.getPersonList();
    }

    @Override
    public Person createOrUpdatePerson(Person person) {
        Person created = personRepository.saveOrUpdate(person);
        personEventDispatcher.notifyCreated(person);
        return created;
    }

    @Override
    public boolean deletePerson(long id) {
        return personRepository.deleteById(id);
    }
}
