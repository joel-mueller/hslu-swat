package ch.hslu.persistence;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;

import java.util.List;
import java.util.UUID;

public interface Database {
    String testDB();

    Customer getCustomer(UUID id);

    Book getBook(int id);

    boolean addBook(Book book);

    boolean addCustomer(Customer customer);

    List<BorrowRecord> getRecords(RecordFilter filter);

    boolean addBorrowRecord(BorrowRecord record);

    boolean updateBorrowRecord(BorrowRecord record);
}
