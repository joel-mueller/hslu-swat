package ch.hslu.api;

import ch.hslu.business.Library;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Api {
    private final Database connector;
    private final Library library;
    private static final Logger LOG = LoggerFactory.getLogger(Api.class);

    public Api() {
        this.connector = new DatabaseConnector();
        this.library = new Library(this.connector);
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/test")
    public String testDB() {
        return connector.testDB();
    }
}
