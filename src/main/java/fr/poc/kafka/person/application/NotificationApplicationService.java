package fr.poc.kafka.person.application;

import fr.poc.kafka.openapi.model.PersonNotification;
import fr.poc.kafka.person.infrastructure.mappers.NotificationMapper;
import fr.poc.kafka.person.infrastructure.primary.kafka.PersonConsumer;
import org.springframework.stereotype.Service;

@Service
public class NotificationApplicationService implements INotificationApplicationService {

    private final PersonConsumer personConsumer;
    private final NotificationMapper notificationMapper;

    public NotificationApplicationService(PersonConsumer personConsumer, NotificationMapper notificationMapper) {
        this.personConsumer = personConsumer;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public PersonNotification getPersonNotification() {

        return notificationMapper.mapToPersonNotification(personConsumer.readSingleNextMessage());
    }
}
