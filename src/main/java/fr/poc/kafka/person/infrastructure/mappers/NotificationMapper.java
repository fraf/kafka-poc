package fr.poc.kafka.person.infrastructure.mappers;

import fr.poc.kafka.openapi.model.PersonNotification;
import fr.poc.kafka.person.infrastructure.secondary.kafka.PersonMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface NotificationMapper {

    @Mapping(source = "payload", target = "personne")
    PersonNotification mapToPersonNotification(PersonMessage personMessage);

}
