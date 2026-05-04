package fr.poc.kafka.person.infrastructure.secondary.repository;

import fr.poc.kafka.person.domain.ent.Person;
import fr.poc.kafka.person.domain.ports.secondary.IPersonRepository;
import fr.poc.kafka.person.infrastructure.mappers.PersonMapper;
import fr.poc.kafka.person.infrastructure.secondary.entities.PersonEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonPostgresAdapter extends SimpleJpaRepository<PersonEntity, Long> implements IPersonRepository {

    private final PersonMapper personMapper;

    public PersonPostgresAdapter(EntityManager entityManager, PersonMapper personMapper) {
        super(JpaEntityInformationSupport.getEntityInformation(PersonEntity.class, entityManager), entityManager);
        this.personMapper = personMapper;
    }

    @Override
    public List<Person> getPersonList() {
        return super.findAll().stream().map(personMapper::mapToPerson).toList();
    }

    @Override
    public Optional<Person> getById(long id) {
        return super.findById(id).map(personMapper::mapToPerson);
    }

    @Override
    public Person saveOrUpdate(Person person) {
        return personMapper.mapToPerson(super.save(personMapper.mapToPersonEntity(person)));
    }


    @Override
    public boolean deleteById(long id) {
        super.deleteById(id);
        return true;
    }
}
