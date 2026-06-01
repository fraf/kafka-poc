package fr.poc.kafka.person.application;

import fr.poc.kafka.openapi.model.PersonNotification;

public interface INotificationApplicationService {

    PersonNotification getPersonNotification();
}
