package ch.hslu.api;

import ch.hslu.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/api")
public class Api {
    private DatabaseConnector connector;
    private static final Logger LOG = LoggerFactory.getLogger(Api.class);

    public Api() {
        this.connector = new DatabaseConnector();
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/test")
    public String testDB() {
        String query = "SELECT NOW()";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                LOG.info("Current Database Time: {}", rs.getString(1));
                return "Current Database Time: " + rs.getString(1);
            }
        } catch (SQLException e) {
            LOG.error("Failed to connect to database");
        }
        return "Could not connect to database";
    }
}
