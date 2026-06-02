package fr.poc.kafka.config;

import fr.poc.kafka.person.infrastructure.secondary.kafka.PersonMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Topic Naming convention : <business domain>.<data description>.<classification>
 */
@Configuration//(exclude = org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration.class)
public class KafkaConfiguration {

    public static final String PERSON_TOPIC_NAME = "KAFKAPOC.PERSON.CRUD";
    public static final String PERSON_CONSUMER_GROUP1_ID = "personConsumerGroup1Id";
    public static final String PERSON_CONSUMER_CLIENT_ID = "personConsumerClientId";

    private static final int PERSON_TOPIC_PARTITION_COUNT = 2;
    //The total number of replicas including the leader constitute the replication factor
    private static final int PERSON_TOPIC_REPLICAS_COUNT = 1;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(PERSON_TOPIC_NAME)
                .partitions(PERSON_TOPIC_PARTITION_COUNT)
                .replicas(PERSON_TOPIC_REPLICAS_COUNT)
                .build();
    }

    /**
     * surcharge le fichier application.yml qui est appliqué à @KafkaListener.
     * Ici, on configure à la main le consumer car je crée un consumerOnDemand (besoin de coder le poll)
     * @param kafkaProperties properties de configuration pour Kafka spring
     * @return un consumer factory paramétré
     */
    @Bean
    public ConsumerFactory<String, PersonMessage> personConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());

        // !!! LIGNE À AJOUTER : On s'assure que le consumer écoute le MÊME broker que le reste du test
        // Spring Boot injecte généralement la propriété système "spring.kafka.bootstrap-servers" lors des tests
        String bootstrapServers = System.getProperty("spring.kafka.bootstrap-servers");
        if (bootstrapServers != null) {
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        }

        // 1. On configure les classes de désérialisation
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2. On passe les configurations spécifiques au JacksonJsonDeserializer via la MAP de propriétés
        // Cela remplace le .addTrustedPackages() et le constructeur typé
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "fr.poc.kafka.person.infrastructure.secondary.kafka");
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, PersonMessage.class.getName());
        // ^ TRÈS IMPORTANT car ton producer a mis [spring.json.add.type.headers]: false
        // Sans header de type, le consumer DOIT savoir par défaut vers quelle classe désérialiser !

        // 3. On utilise le constructeur simple qui prend UNIQUEMENT la map de propriétés
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
