package ch.hslu.persistence;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
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
                return new Customer(id, rs.getString("first_name"), rs.getString("last_name"), rs.getString("street"),
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
                return new Book(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"), rs.getString("author"),
                        rs.getString("year"), rs.getString("publisher"), rs.getString("imageUrlS"),
                        rs.getString("imageUrlM"), rs.getString("imageUrlL"));
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
        PreparedStatement stmt;
        List<Object> parameters = new ArrayList<>();
        String statementString = "SELECT * FROM borrow_records WHERE";
        // Loop once to set the statement string
        if (filter.getId() != null) {
            statementString += " id = ?";
            parameters.add(filter.getId().toString());
        }
        if (filter.getIdBook() != null) {
            statementString += " book_id = ?";
            parameters.add(filter.getIdBook());
        }
        if (filter.getIdCustomer() != null) {
            statementString += " customer_id = ?";
            parameters.add(filter.getIdCustomer().toString());
        }
        if (filter.getDateBorrowedBefore() != null) {
            statementString += " date_borrowed < ?";
            parameters.add(Date.valueOf(filter.getDateBorrowedBefore()));
        }
        if (filter.getDateBorrowedAfter() != null) {
            statementString += " date_borrowed > ?";
            parameters.add(Date.valueOf(filter.getDateBorrowedAfter()));
        }
        if (filter.getLongerThen() != null) {
            statementString += " duration_days > ?";
            parameters.add(filter.getLongerThen().getDays());
        }
        if (filter.getShorterThen() != null) {
            statementString += " duration_days < ?";
            parameters.add(filter.getShorterThen().getDays());
        }
        if (filter.getReturned() != null) {
            statementString += " returned = ?";
            parameters.add(filter.getReturned());
        }

        try {
            stmt = connection.prepareStatement(statementString);
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<BorrowRecord> recordList = new ArrayList<>();
            while (rs.next()) {
                recordList.add(new BorrowRecord.Builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .bookId(rs.getInt("book_id"))
                        .customerId(UUID.fromString(rs.getString("customer_id")))
                        .dateBorrowed(LocalDate.parse(rs.getString("date_borrowed")))
                        .duration(Period.ofDays(rs.getInt("durations_days")))
                        .returned(rs.getBoolean("borrowed"))
                        .build());
            }
            return recordList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addBorrowRecord(BorrowRecord record) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(
                    "INSERT INTO borrow_records(uuid, book_id, customer_id, date_borrowed, duration_days, borrowed) VALUES(?,?,?,?,?,?)");
            stmt.setString(1, record.getId().toString());
            stmt.setInt(2, record.getBookId());
            stmt.setString(3, record.getCustomerId().toString());
            stmt.setString(4, record.getDateBorrowed().toString());
            stmt.setInt(5, record.getDuration().getDays());
            stmt.setBoolean(6, record.isReturned());
            return stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord record) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(
                    "UPDATE borrow_records SET book_id = ?, customer_id ?, date_borrowed = ?, duration = ?, returned = ? WHERE id = ?");
            stmt.setInt(1, record.getBookId());
            stmt.setString(2, record.getCustomerId().toString());
            stmt.setString(3, record.getDateBorrowed().toString());
            stmt.setInt(4, record.getDuration().getDays());
            stmt.setBoolean(5, record.isReturned());
            stmt.setString(6, record.getId().toString());
            return stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
