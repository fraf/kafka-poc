package fr.poc.kafka.person.infrastructure.primary.rest;

import fr.poc.kafka.openapi.model.PersonNotification;
import fr.poc.kafka.openapi.rest.NotificationApi;
import fr.poc.kafka.person.application.NotificationApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationResource implements NotificationApi {

    private final NotificationApplicationService notificationApplicationService;

    public NotificationResource(NotificationApplicationService notificationApplicationService) {
        this.notificationApplicationService = notificationApplicationService;
    }

    @Override
    public ResponseEntity<PersonNotification> getNotification() {
        PersonNotification personNotification = notificationApplicationService.getPersonNotification();
        return new ResponseEntity<>(personNotification, HttpStatus.OK);
    }
}
