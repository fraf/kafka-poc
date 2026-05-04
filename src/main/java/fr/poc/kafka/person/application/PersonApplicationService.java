package fr.poc.kafka.person.application;

import fr.poc.kafka.person.domain.ports.primary.IPersonUseCase;
import fr.poc.kafka.person.infrastructure.mappers.PersonMapper;
import fr.poc.kafka.person.infrastructure.primary.dtos.PersonDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Responsable de :
 * <ul>
 * <li>De l'orchestration simple (pas de Métier) des opérations ;</li>
 * <li>De la gestion des transactions ;</li>
 * <li>De la gestion des authorizations (que je fais avec kipe).</li>
 * </ul>
 */
@Service
@Transactional
public class PersonApplicationService implements IPersonApplicationService {

    private final IPersonUseCase personUseCase;
    private final PersonMapper personMapper;

    public PersonApplicationService(IPersonUseCase personUseCase, PersonMapper personMapper) {
        this.personUseCase = personUseCase;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDto getPersonDto(long id) {
        return personUseCase.getPerson(id).map(personMapper::mapToPersonDto).orElse(null);
    }

    @Override
    public List<PersonDto> getPersonDtoList() {
        return personUseCase.getPersonList().stream().map(personMapper::mapToPersonDto).toList();
    }

    @Override
    public PersonDto createOrUpdatePersonDto(PersonDto personDto) {
        return personMapper.mapToPersonDto(personUseCase.createOrUpdatePerson(personMapper.mapToPerson(personDto)));
    }
}
