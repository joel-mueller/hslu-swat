package ch.hslu.persistence;

import ch.hslu.entities.Customer;
import org.junit.jupiter.api.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseConnectorTest {
    static String dbName = "mydatabase";
    static String dbUser = "myuser";
    static String password = "secret";

    static DatabaseConnector connector;
    static Map<UUID, Customer> customers = new HashMap<>();

    static Logger LOG = LoggerFactory.getLogger(DatabaseConnectorTest.class);

    @Container
    static MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
            .withDatabaseName(dbName).withUsername(dbUser).withPassword(password).withCopyFileToContainer(
                    MountableFile.forHostPath("config/mysql/database.sql"), "/docker-entrypoint-initdb.d/");

    @BeforeAll
    static void setup() {
        container.start();
        connector = new DatabaseConnector(container.getJdbcUrl(), dbUser, password);
    }

    @Test
    @Order(1)
    void addCustomer() {
        Customer c = new Customer(UUID.randomUUID(), "John", "Pork", "Bahnhofstrasse 3", "8570");
        customers.put(c.id(), c);
        assertDoesNotThrow(() -> connector.addCustomer(c));
    }

    @Test
    @Order(2)
    void getCustomer() {
        Set<UUID> set = customers.keySet();
        Iterator<UUID> iterator = set.iterator();
        if (iterator.hasNext()) {
            UUID id = iterator.next();
            Customer expected = customers.get(id);
            Customer received = connector.getCustomer(id);
            assertEquals(expected, received);
        }
    }
}
