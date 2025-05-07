package ch.hslu.persistence;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;
import org.junit.jupiter.api.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseConnectorTest {
    static String dbName = "mydatabase";
    static String dbUser = "myuser";
    static String password = "secret";

    static DatabaseConnector connector;
    static Map<UUID, Customer> customers = new LinkedHashMap<>();
    static Map<Integer, Book> books = new LinkedHashMap<>();
    static Map<UUID, BorrowRecord> borrowRecords = new LinkedHashMap<>();

    static Logger LOG = LoggerFactory.getLogger(DatabaseConnectorTest.class);

    @Container
    static MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.36"))
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
        Customer c = new Customer(UUID.randomUUID(), "John", "Pork", "Bahnhofstrasse 3", "9854");
        customers.put(c.id(), c);
        assertDoesNotThrow(() -> connector.addCustomer(c));
    }

    @Test
    @Order(1)
    void addCustomers() {
        Customer c1 = new Customer(UUID.randomUUID(), "Dave", "Stark", "Silvanstrasse 7", "3401");
        Customer c2 = new Customer(UUID.randomUUID(), "Mia", "Peter", "Goldstrasse 1", "6600");
        customers.put(c1.id(), c1);
        customers.put(c2.id(), c2);
        assertDoesNotThrow(() -> connector.addCustomer(c1));
        assertDoesNotThrow(() -> connector.addCustomer(c2));
    }

    @Test
    @Order(1)
    void addBook() {
        Book b = new Book(1, "978-3-16-148410-0", "The Great Gatsby", "F. Scott Fitzgerald", "1925", "Scribner",
                "small.jpg", "medium.jpg", "large.jpg");
        books.put(b.id(), b);
        assertDoesNotThrow(() -> connector.addBook(b));
    }

    @Test
    @Order(1)
    void addBooks() {
        Book b1 = new Book(2, "978-3-16-148410-0", "Book One", "Author One", "2000", "Publisher One", "small1.jpg",
                "medium1.jpg", "large1.jpg");
        Book b2 = new Book(3, "978-3-16-148410-0", "Book Two", "Author Two", "2010", "Publisher Two", "small2.jpg",
                "medium2.jpg", "large2.jpg");
        books.put(b1.id(), b1);
        books.put(b2.id(), b2);
        assertDoesNotThrow(() -> connector.addBook(b1));
        assertDoesNotThrow(() -> connector.addBook(b2));
    }

    @Test
    @Order(2)
    void addBorrowRecords() {
        List<UUID> customerIdList = new ArrayList<>(customers.keySet());
        BorrowRecord br1 = new BorrowRecord.Builder().id(UUID.randomUUID()).bookId(1).customerId(customerIdList.get(0))
                .dateBorrowed(LocalDate.of(2024, 4, 1)).duration(Period.ofDays(30)).returned(false).build();
        BorrowRecord br2 = new BorrowRecord.Builder().id(UUID.randomUUID()).bookId(2).customerId(customerIdList.get(0))
                .dateBorrowed(LocalDate.of(2024, 4, 3)).duration(Period.ofDays(30)).returned(false).build();
        BorrowRecord br3 = new BorrowRecord.Builder().id(UUID.randomUUID()).bookId(3).customerId(customerIdList.get(1))
                .dateBorrowed(LocalDate.of(2024, 4, 3)).duration(Period.ofDays(30)).returned(true).build();
        BorrowRecord br4 = new BorrowRecord.Builder().id(UUID.randomUUID()).bookId(1).customerId(customerIdList.get(1))
                .dateBorrowed(LocalDate.of(2022, 4, 1)).duration(Period.ofDays(35)).returned(true).build();
        borrowRecords.put(br1.getId(), br1);
        borrowRecords.put(br2.getId(), br2);
        borrowRecords.put(br3.getId(), br3);
        borrowRecords.put(br4.getId(), br4);
        assertDoesNotThrow(() -> connector.addBorrowRecord(br1));
        assertDoesNotThrow(() -> connector.addBorrowRecord(br2));
        assertDoesNotThrow(() -> connector.addBorrowRecord(br3));
        assertDoesNotThrow(() -> connector.addBorrowRecord(br4));
    }

    @Test
    @Order(2)
    void getCustomers() {
        Set<UUID> set = customers.keySet();
        Iterator<UUID> iterator = set.iterator();
        if (iterator.hasNext()) {
            UUID id = iterator.next();
            Customer expected = customers.get(id);
            Customer received = connector.getCustomer(id);
            assertEquals(expected, received);
        }
    }

    @Test
    @Order(2)
    void getBooks() {
        Set<Integer> set = books.keySet();
        Iterator<Integer> iterator = set.iterator();
        if (iterator.hasNext()) {
            int id = iterator.next();
            Book expected = books.get(id);
            Book received = connector.getBook(id);
            assertEquals(expected, received);
        }
    }

    @Test
    @Order(3)
    void getBorrowRecords() {
        Set<UUID> set = borrowRecords.keySet();
        Iterator<UUID> iterator = set.iterator();
        if (iterator.hasNext()) {
            UUID id = iterator.next();
            BorrowRecord expected = borrowRecords.get(id);
            List<BorrowRecord> received = connector.getRecords(new RecordFilter.Builder().id(id).build());
            assertEquals(expected, received.get(0));
        }
    }

    @Test
    @Order(3)
    void getBorrowRecordsByBookId() {
        int bookId = 1;
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br1 = borrowRecords.get(list.get(0));
        BorrowRecord br4 = borrowRecords.get(list.get(3));
        RecordFilter filter = new RecordFilter.Builder().idBook(bookId).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br1));
        assertTrue(retrievedList.contains(br4));
    }

    @Test
    @Order(3)
    void getBorrowRecordsByCustomerId() {
        List<UUID> customerIdList = new ArrayList<>(customers.keySet());
        UUID customerId = customerIdList.get(0);
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br1 = borrowRecords.get(list.get(0));
        BorrowRecord br2 = borrowRecords.get(list.get(1));
        RecordFilter filter = new RecordFilter.Builder().idCustomer(customerId).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br1));
        assertTrue(retrievedList.contains(br2));
    }

    @Test
    @Order(3)
    void getBorrowRecordsByDateBorrowedBefore() {
        LocalDate date = LocalDate.of(2024, 4, 2);
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br1 = borrowRecords.get(list.get(0));
        BorrowRecord br4 = borrowRecords.get(list.get(3));
        RecordFilter filter = new RecordFilter.Builder().dateBorrowedBefore(date).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br1));
        assertTrue(retrievedList.contains(br4));
    }

    @Test
    @Order(3)
    void getBorrowRecordsByDateBorrowedAfter() {
        LocalDate date = LocalDate.of(2024, 4, 2);
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br2 = borrowRecords.get(list.get(1));
        BorrowRecord br3 = borrowRecords.get(list.get(2));
        RecordFilter filter = new RecordFilter.Builder().dateBorrowedAfter(date).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br2));
        assertTrue(retrievedList.contains(br3));
    }

    @Test
    @Order(3)
    void getBorrowRecordsByDateShorterThan() {
        Period duration = Period.ofDays(31);
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br1 = borrowRecords.get(list.get(0));
        BorrowRecord br2 = borrowRecords.get(list.get(1));
        BorrowRecord br3 = borrowRecords.get(list.get(2));
        RecordFilter filter = new RecordFilter.Builder().shorterThen(duration).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br1));
        assertTrue(retrievedList.contains(br2));
        assertTrue(retrievedList.contains(br3));
    }

    @Test
    @Order(3)
    void getBorrowRecordsByDateLongerThan() {
        Period duration = Period.ofDays(31);
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br4 = borrowRecords.get(list.get(3));
        RecordFilter filter = new RecordFilter.Builder().longerThen(duration).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br4));
    }

    @Test
    @Order(3)
    void getBorrowRecordsByReturned() {
        boolean returned = true;
        List<UUID> list = new ArrayList<>(borrowRecords.keySet());
        BorrowRecord br3 = borrowRecords.get(list.get(2));
        BorrowRecord br4 = borrowRecords.get(list.get(3));
        RecordFilter filter = new RecordFilter.Builder().returned(returned).build();
        List<BorrowRecord> retrievedList = connector.getRecords(filter);
        assertTrue(retrievedList.contains(br3));
        assertTrue(retrievedList.contains(br4));
    }

    @Test
    @Order(3)
    void updateCustomerFirstName() {
        String newFirstName = "Jonathon";
        List<UUID> list = new ArrayList<>(customers.keySet());
        Customer initial = customers.get(list.get(0));
        Customer changed = new Customer(initial.id(), newFirstName, initial.lastName(), initial.street(),
                initial.zipCode());
        assertTrue(connector.updateCustomer(initial.id(), changed));
        Customer retrieved = connector.getCustomer(initial.id());
        assertEquals(newFirstName, retrieved.firstName());
    }

    @Test
    @Order(3)
    void updateCustomerLastName() {
        String newLastName = "Perky";
        List<UUID> list = new ArrayList<>(customers.keySet());
        Customer initial = customers.get(list.get(0));
        Customer changed = new Customer(initial.id(), initial.firstName(), newLastName, initial.street(),
                initial.zipCode());
        assertTrue(connector.updateCustomer(initial.id(), changed));
        Customer retrieved = connector.getCustomer(initial.id());
        assertEquals(newLastName, retrieved.lastName());
    }

    @Test
    @Order(3)
    void updateCustomerStreet() {
        String newStreet = "Zürcherstrasse 880";
        List<UUID> list = new ArrayList<>(customers.keySet());
        Customer initial = customers.get(list.get(1));
        Customer changed = new Customer(initial.id(), initial.firstName(), initial.lastName(), newStreet,
                initial.zipCode());
        assertTrue(connector.updateCustomer(initial.id(), changed));
        Customer retrieved = connector.getCustomer(initial.id());
        assertEquals(newStreet, retrieved.street());
    }

    @Test
    @Order(3)
    void updateCustomerZipCode() {
        String newZipCode = "8001";
        List<UUID> list = new ArrayList<>(customers.keySet());
        Customer initial = customers.get(list.get(1));
        Customer changed = new Customer(initial.id(), initial.firstName(), initial.lastName(), initial.street(),
                newZipCode);
        assertTrue(connector.updateCustomer(initial.id(), changed));
        Customer retrieved = connector.getCustomer(initial.id());
        assertEquals(newZipCode, retrieved.zipCode());
    }

    @Test
    @Order(3)
    void updateCustomerPreventIdChange() {
        UUID newId = UUID.randomUUID();
        List<UUID> list = new ArrayList<>(customers.keySet());
        Customer initial = customers.get(list.get(0));
        Customer changed = new Customer(newId, initial.firstName(), initial.lastName(), initial.street(),
                initial.zipCode());
        assertTrue(connector.updateCustomer(initial.id(), changed));
        Customer retrieved = connector.getCustomer(initial.id());
        assertNotNull(retrieved);
        assertNotEquals(newId, retrieved.id());
        assertEquals(initial.id(), retrieved.id());
    }

    @Test
    @Order(3)
    void updateCustomerAll() {
        String newFirstName = "Stacy";
        String newLastName = "Fakename";
        String newStreet = "Mentopolis 20";
        String newZipCode = "2320";
        List<UUID> list = new ArrayList<>(customers.keySet());
        Customer initial = customers.get(list.get(1));
        Customer changed = new Customer(initial.id(), newFirstName, newLastName, newStreet, newZipCode);
        assertTrue(connector.updateCustomer(initial.id(), changed));
        Customer retrieved = connector.getCustomer(initial.id());
        assertEquals(changed, retrieved);
    }
}
