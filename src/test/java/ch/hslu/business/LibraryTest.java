package ch.hslu.business;

import ch.hslu.entities.Customer;
import ch.hslu.persistence.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    FakeDatabase database;
    Library library;

    @BeforeEach
    void setUp() {
        this.database = new FakeDatabase();
        this.library = new Library(database);
    }

    @Disabled
    void borrowBookCustomerOkBookOk() {
        UUID customer = database.getCustomerNotMaxBooksNothingOverdueUUID();
        assertTrue(library.borrowBook(customer, 8));
    }

    @Test
    void returnBook() {
    }
}
