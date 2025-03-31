package ch.hslu.business;

import ch.hslu.entities.BorrowRecord;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.RecordFilter;

import java.time.Period;
import java.util.List;
import java.util.UUID;

import static ch.hslu.entities.BorrowRecord.calculateOverdue;

public class Library {
    private final Database connector;
    private static final Period BORROW_TIME = Period.ofDays(90);
    private static final int FRANCS_OVERDUE_PER_DAY = 2;
    private static final int MAX_NUMBER_OF_BOOKS = 4;

    public Library(Database connector) {
        this.connector = connector;
    }

    // TODO String zurück geben mit response für api
    public boolean borrowBook(UUID customerId, int idBook) {
        RecordFilter filter = new RecordFilter.Builder().id(customerId).returned(false).build();
        List<BorrowRecord> records = connector.getRecords(filter);
        if (records.size() > MAX_NUMBER_OF_BOOKS)
            return false;
        if (records.stream().mapToInt(BorrowRecord::calculateOverdue).sum() == 0)
            return false;
        return connector.addBorrowRecord(new BorrowRecord.Builder().bookId(idBook).customerId(customerId).build());
    }

    // TODO String zurück geben mit response für api
    public int returnBook(UUID userId, int idBook) {
        RecordFilter filter = new RecordFilter.Builder().idBook(idBook).idCustomer(userId).build();
        List<BorrowRecord> records = this.connector.getRecords(filter);
        if (records.isEmpty()) {
            return 0;
        }
        BorrowRecord record = records.getFirst();
        record.setReturned(true);
        connector.updateBorrowRecord(record);
        return calculateOverdue(record) * FRANCS_OVERDUE_PER_DAY;
    }
}
