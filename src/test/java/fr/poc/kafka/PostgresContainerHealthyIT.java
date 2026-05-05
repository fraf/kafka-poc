package fr.poc.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class PostgresContainerHealthyIT extends AbstractIntegrationTestsBase {


    @Test
    public void whenListTableOfSchemaQueryExecuted_thenResultsReturned() throws Exception {
        String listTablesQuery = """
                SELECT table_name
                 FROM information_schema.tables
                 WHERE table_schema = current_schema()
                 AND table_type = 'BASE TABLE'
                 ORDER BY table_name;""";

        ResultSet resultSet = performQuery(postgres, listTablesQuery);
        if (resultSet.next()) {
            log.info("Tables list :{}", resultSet.getArray(1));
        } else {
            Assertions.fail("Aucune table ! Au moins 'flyway_schema_history' attendu.");
        }
    }


    private int performUpdate(PostgreSQLContainer<?> postgreSQLContainer, String query) throws SQLException {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        return conn.createStatement().executeUpdate(query);
    }

    private ResultSet performQuery(PostgreSQLContainer<?> postgreSQLContainer, String query) throws SQLException {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        return conn.createStatement().executeQuery(query);
    }

    @Test
    void container_is_running() {
        Assertions.assertTrue(postgres.isRunning());
    }

    @Test
    void datasource_details_are_correct() {
        assertEquals("org.postgresql.Driver", postgres.getDriverClassName());
        assertEquals("test", postgres.getDatabaseName());
        assertEquals("test", postgres.getUsername());
        assertEquals("test", postgres.getPassword());

    }
}
