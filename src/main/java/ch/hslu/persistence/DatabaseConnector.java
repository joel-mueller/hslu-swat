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
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement("SELECT * FROM customers WHERE id = ?");
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(id,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("street"),
                        rs.getString("zip_code"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO updateCustomer() deleteCustomer() addCustomer()

    @Override
    public Book getBook(int id) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement("SELECT * FROM books WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getInt("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("year"),
                        rs.getString("publisher"),
                        rs.getString("imageUrlS"),
                        rs.getString("imageUrlM"),
                        rs.getString("imageUrlL"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO updateBook() deleteBook() (maybe also retire instead of delete) addBook()

    @Override
    public List<BorrowRecord> getRecords(RecordFilter filter) {
        return new ArrayList<BorrowRecord>();
    }

    @Override
    public boolean addBorrowRecord(BorrowRecord record) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement("INSERT INTO borrow_records(uuid, book_id, customer_id, date_borrowed, duration_days, borrowed) VALUES(?,?,?,?,?,?)");
            stmt.setString(1, record.id().toString());
            stmt.setInt(2, record.bookId());
            stmt.setString(3, record.customerId().toString());
            stmt.setString(4, record.dateBorrowed().toString());
            stmt.setInt(5, record.duration().getDays());
            stmt.setBoolean(6, record.returned());
            return stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord record) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement("UPDATE borrow_records SET book_id = ?, customer_id ?, date_borrowed = ?, duration = ?, returned = ? WHERE id = ?");
            stmt.setInt(1, record.bookId());
            stmt.setString(2, record.customerId().toString());
            stmt.setString(3, record.dateBorrowed().toString());
            stmt.setInt(4, record.duration().getDays());
            stmt.setBoolean(5, record.returned());
            stmt.setString(6, record.id().toString());
            return stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
