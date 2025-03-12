package ch.hslu.api;

import ch.hslu.entities.Book;
import ch.hslu.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Api {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/testmysql")
    public @ResponseBody Iterable<Book> testMysql() {
        Book b = new Book((int)(Math.random() * 1000000) + 1 , "test","test","test","test","test","test","test","test");
        bookRepository.save(b);
        return bookRepository.findAll();
    }
}