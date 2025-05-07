package ch.hslu.api;

import ch.hslu.business.Library;
import ch.hslu.dto.*;
import ch.hslu.entities.Book;
import ch.hslu.entities.CSVReader;
import ch.hslu.entities.Customer;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // curl -X POST http://localhost:8080/api/books/exampleBooks
    @PostMapping("/books/exampleBooks")
    public String exampleBooks() {
        List<Book> books = CSVReader.getBooks();
        for (int i = 0; i < 500; i++) {
            connector.addBook(books.get(i));
        }
        return "Successfully wrote books";
    }

    @PostMapping("/customer")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest request) {
        Customer customer = new Customer(UUID.randomUUID(), request.firstName(), request.lastName(), request.street(),
                request.zipCode());
        boolean successfully = connector.addCustomer(customer);
        if (!successfully)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer could not got written to the database");
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowBookRequest request) {
        try {
            BorrowBookResponse borrow = library.borrowBook(request);
            return ResponseEntity.ok(borrow);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody ReturnBookRequest request) {
        try {
            ReturnBookResponse returned = library.returnBook(request);
            return ResponseEntity.ok(returned);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
