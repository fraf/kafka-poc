package fr.poc.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class KafkaContainerHealthyIT extends AbstractIntegrationTestsBase {

    @Test
    void kafka_should_be_healthy_and_operational() throws Exception {
        // Étape 1 : Vérification basique au niveau du moteur Docker
        assertThat(kafka.isRunning())
                .as("Le conteneur Docker Kafka devrait être en cours d'exécution")
                .isTrue();

        // Étape 2 : Vérification applicative (Le broker est-il prêt à répondre ?)
        Map<String, Object> config = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "5000" // Timeout court pour le test
        );

        log.info("Connexion au broker via l'AdminClient pour validation de santé...");

        try (AdminClient adminClient = AdminClient.create(config)) {
            // On demande l'ID du cluster. Si Kafka a mal démarré ou est figé,
            // cette ligne lèvera une exception ou bloquera jusqu'au timeout.
            String clusterId = adminClient.describeCluster()
                    .clusterId()
                    .get(5, TimeUnit.SECONDS);

            log.info("🔥 Statut : HEALTHY. Connecté au cluster Kafka. ID : {}", clusterId);

            assertThat(clusterId)
                    .as("L'ID du cluster Kafka ne doit pas être vide")
                    .isNotBlank();
        }
    }
}
