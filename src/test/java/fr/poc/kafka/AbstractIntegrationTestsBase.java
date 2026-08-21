package fr.poc.kafka;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 🚀 On utilise un Initializer pour injecter les propriétés AVANT même qu'Hibernate ou Flyway ne s'éveillent
@ContextConfiguration(initializers = AbstractIntegrationTestsBase.Initializer.class)
public abstract class AbstractIntegrationTestsBase {

    protected static final PostgreSQLContainer<?> postgres;
    protected static final KafkaContainer kafka;

    static {
        log.info("⚙️ Démarrage des conteneurs isolés du cycle de vie Spring...");
        kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0")).withReuse(false);
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:18.3"))
                // Copie le fichier du classpath vers le container
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("datasets/insert_personnes.csv"),
                        "/tmp/insert_personnes.csv"
                )
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("datasets/insert_accounts.csv"),
                        "/tmp/insert_accounts.csv"
                )
                .withReuse(false);

        kafka.start();
        postgres.start();
    }

    // 🚀 L'Initializer garantit l'ordre d'exécution absolu pour le Context de Spring
    // L'Initializer garantit l'ordre d'exécution absolu pour le Context de Spring
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            log.info("🔗 Injection prioritaire des ports conteneurs dans l'environnement de test...");

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),

                    // 1. Spécifier le VRAI driver pour court-circuiter le driver automatique de Testcontainers
                    "spring.datasource.driver-class-name=org.postgresql.Driver",
                    "jakarta.persistence.jdbc.driver=org.postgresql.Driver",

                    // 2. Configuration Datasource Standard (Spring)
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),

                    // 3. Configuration JPA / Hibernate (Jakarta)
                    "jakarta.persistence.jdbc.url=" + postgres.getJdbcUrl(),
                    "jakarta.persistence.jdbc.user=" + postgres.getUsername(),
                    "jakarta.persistence.jdbc.password=" + postgres.getPassword(),
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",

                    // 4. Configuration Flyway
                    "spring.flyway.url=" + postgres.getJdbcUrl(),
                    "spring.flyway.user=" + postgres.getUsername(),
                    "spring.flyway.password=" + postgres.getPassword()
            );
        }
    }

    @Autowired
    protected Flyway flyway;

    private static final Set<String> INITIALIZED_CLASSES = new HashSet<>();

    @BeforeEach
    void resetPostgresOncePerTestClass() {
        String className = getClass().getName();
        if (INITIALIZED_CLASSES.add(className)) {
            log.info("🧹 Reset unique pour la classe de test : Clean & Migrate... {}", getClass().getSimpleName());
            flyway.clean();
            flyway.migrate();
        }
    }
}
