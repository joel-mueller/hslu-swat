package ch.hslu.api;

import ch.hslu.business.Library;
import ch.hslu.dto.BorrowBook;
import ch.hslu.dto.ReturnBook;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam UUID customerId, @RequestParam int bookId) {
        try {
            BorrowBook borrow = library.borrowBook(customerId, bookId);
            return ResponseEntity.ok(borrow);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestParam UUID customerId, @RequestParam int bookId) {
        try {
            ReturnBook returned = library.returnBook(customerId, bookId);
            return ResponseEntity.ok(returned);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
