package ch.hslu.business;

import ch.hslu.dto.BorrowBook;
import ch.hslu.dto.ReturnBook;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.RecordFilter;

import java.time.Period;
import java.util.List;
import java.util.UUID;

import static ch.hslu.entities.BorrowRecord.calculateDaysOverdue;

public class Library {
    private final Database connector;
    public static final Period BORROW_TIME = Period.ofDays(90);
    public static final int FRANCS_OVERDUE_PER_DAY = 2;
    public static final int MAX_NUMBER_OF_BOOKS = 4;

    public Library(Database connector) {
        this.connector = connector;
    }

    public BorrowBook borrowBook(UUID customerId, int idBook) throws IllegalStateException {
        if (!bookIsAvailable(idBook))
            throw new IllegalStateException("The book is not available for borrowing.");
        RecordFilter filter = new RecordFilter.Builder().idCustomer(customerId).returned(false).build();
        List<BorrowRecord> records = connector.getRecords(filter);
        if (records.size() >= MAX_NUMBER_OF_BOOKS) {
            throw new IllegalStateException("The customer has already borrowed the maximum number of books.");
        }
        if (records.stream().mapToInt(BorrowRecord::calculateDaysOverdue).sum() != 0) {
            throw new IllegalStateException("The customer has overdue books.");
        }
        BorrowRecord record = new BorrowRecord.Builder().bookId(idBook).customerId(customerId).build();
        boolean written = connector.addBorrowRecord(record);
        if (!written)
            throw new IllegalStateException("Failed to write the borrow record to the database.");
        return new BorrowBook(record.getCustomerId(), record.getBookId(),
                record.getDateBorrowed().plus(record.getDuration()));
    }

    protected boolean bookIsAvailable(int idBook) {
        RecordFilter filter = new RecordFilter.Builder().idBook(idBook).returned(false).build();
        List<BorrowRecord> records = connector.getRecords(filter);
        return records.isEmpty();
    }

    public ReturnBook returnBook(UUID userId, int idBook) throws IllegalStateException {
        RecordFilter filter = new RecordFilter.Builder().idBook(idBook).idCustomer(userId).returned(false).build();
        List<BorrowRecord> records = this.connector.getRecords(filter);
        if (records.isEmpty()) {
            throw new IllegalStateException("No open book record for this book and this customer");
        }
        BorrowRecord record = records.getFirst();
        record.setReturned(true);
        boolean written = connector.updateBorrowRecord(record);
        if (!written)
            throw new IllegalStateException("Failed to write the borrow record to the database.");
        return new ReturnBook(true, calculateDaysOverdue(record) * FRANCS_OVERDUE_PER_DAY);
    }

}
