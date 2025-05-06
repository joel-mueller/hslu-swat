package ch.hslu.business;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.RecordFilter;

import java.util.List;
import java.util.UUID;

public class FakeDatabase implements Database {
    private final List<Customer> customerList;
    private final List<Book> bookList;
    private final List<BorrowRecord> borrowRecordList;

    public FakeDatabase(List<Customer> customerList, List<Book> bookList, List<BorrowRecord> borrowRecordList) {
        this.customerList = customerList;
        this.bookList = bookList;
        this.borrowRecordList = borrowRecordList;
    }

    @Override
    public String testDB() {
        return "";
    }

    @Override
    public Customer getCustomer(UUID id) {
        return this.customerList.stream().filter(customer -> customer.id().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Book getBook(int id) {
        return this.bookList.stream().filter(book -> book.id() == id).findFirst().orElse(null);
    }

    @Override
    public boolean addBook(Book book) {
        return false;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        return false;
    }

    @Override
    public List<BorrowRecord> getRecords(RecordFilter filter) {
        List<BorrowRecord> filtered = this.borrowRecordList.stream().filter(record -> record.filter(filter)).toList();
        return List.copyOf(filtered);
    }

    @Override
    public boolean addBorrowRecord(BorrowRecord record) {
        this.borrowRecordList.add(record);
        return true;
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord record) {
        List<BorrowRecord> records = getRecords(new RecordFilter.Builder().id(record.getId()).build());
        if (records.size() != 1) {
            return false;
        }
        BorrowRecord previousRecord = records.getFirst();
        this.borrowRecordList.remove(previousRecord);
        this.borrowRecordList.add(record);
        return true;
    }
}
