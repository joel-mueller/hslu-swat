package ch.hslu.persistence;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseConnector {
    public DatabaseConnector() {

    }

    public Customer getCustomer(int id) {
        return new Customer(id, "Max", "Mustermann", "Stree1", "3355");
    }

    // TODO updateCustomer() deleteCustomer() addCustomer()

    public Book getBook(int id) {
        return new Book(id, "123412341234", "Hello Title", "John", "2015", "Exlibris", "ImageUrl", "Image url medium", "Image url large");
    }

    // TODO updateBook() deleteBook() (maybe also retire instead of delete) addBook()

    public Record getRecord(UUID Id) {
        return new BorrowRecord(UUID.randomUUID(), 12, 10, LocalDate.now(), Period.ofMonths(3), false);
    }

    public List<BorrowRecord> getRecords(RecordFilter filter) {
        return new ArrayList<BorrowRecord>();
    }
}
