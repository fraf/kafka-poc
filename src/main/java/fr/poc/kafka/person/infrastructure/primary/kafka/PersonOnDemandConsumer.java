package fr.poc.kafka.person.infrastructure.primary.kafka;

import fr.poc.kafka.config.KafkaConfiguration;
import fr.poc.kafka.person.infrastructure.secondary.kafka.PersonMessage;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.poc.kafka.config.KafkaConfiguration.PERSON_CONSUMER_CLIENT_ID;
import static fr.poc.kafka.config.KafkaConfiguration.PERSON_CONSUMER_GROUP1_ID;

@Component
public class PersonOnDemandConsumer {

    private final ConsumerFactory<String, PersonMessage> consumerFactory;

    public PersonOnDemandConsumer(ConsumerFactory<String, PersonMessage> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    /**
     * Polls the person topic once with the provided timeout and returns the list of messages (may be empty).
     * The underlying consumer is closed after the poll to avoid long-running loops.
     */
    public List<PersonMessage> pollOnce(Duration timeout) {
        Consumer<String, PersonMessage> consumer = consumerFactory.createConsumer(PERSON_CONSUMER_GROUP1_ID, PERSON_CONSUMER_CLIENT_ID);
        consumer.subscribe(Collections.singletonList(KafkaConfiguration.PERSON_TOPIC_NAME));
        try {
            ConsumerRecords<String, PersonMessage> records = consumer.poll(timeout);
            List<PersonMessage> results = new ArrayList<>();
            for (ConsumerRecord<String, PersonMessage> record : records) {
                results.add(record.value());
            }
            return results;
        } finally {
            try {
                consumer.unsubscribe();
            } catch (Exception ignored) {
            }
            consumer.close();
        }
    }
}
