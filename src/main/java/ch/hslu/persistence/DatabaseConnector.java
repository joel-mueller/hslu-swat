package ch.hslu.persistence;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO interface machen damit man die DB Mocken kann
public class DatabaseConnector implements Database {
    private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USER = "myuser";
    private static final String PASSWORD = "secret";
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);
    private Connection connection;

    public DatabaseConnector() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOG.info("Connected to MySQL database successfully!");
        } catch (SQLException e) {
            LOG.error("Could not connect to database");
        }
    }

    @Override
    public String testDB() {
        String query = "SELECT NOW()";
        try (Statement stmt = this.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                LOG.info("Current Database Time: {}", rs.getString(1));
                return "Current Database Time: " + rs.getString(1);
            }
        } catch (SQLException e) {
            LOG.error("Failed to connect to database");
        }
        return "Failed to connect to database";
    }

    @Override
    public Customer getCustomer(UUID id) {
        return new Customer(id, "Max", "Mustermann", "Stree1", "3355");
    }

    // TODO updateCustomer() deleteCustomer() addCustomer()

    @Override
    public Book getBook(int id) {
        return new Book(id, "123412341234", "Hello Title", "John", "2015", "Exlibris", "ImageUrl", "Image url medium",
                "Image url large");
    }

    // TODO updateBook() deleteBook() (maybe also retire instead of delete) addBook()

    @Override
    public List<BorrowRecord> getRecords(RecordFilter filter) {
        return new ArrayList<BorrowRecord>();
    }

    @Override
    public boolean addBorrowRecord(BorrowRecord record) {
        return true;
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord record) {
        return true;
    }
}
