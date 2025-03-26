package ch.hslu.business;

import ch.hslu.entities.BorrowRecord;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.RecordFilter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

public class Library {
    private final Database connector;
    private static final Period BORROW_TIME = Period.ofDays(90);
    private static final int FRANCS_OVERDUE_PER_DAY = 2;

    public Library(Database connector) {
        this.connector = connector;
    }

    // borrow book, check if user has already 5 books borrowed and no fine
    public boolean borrowBook(UUID customerId, int idBook) {
        RecordFilter filter = new RecordFilter.Builder().id(customerId).returned(false).build();
        List<BorrowRecord> records = connector.getRecords(filter);
        if (records.size() > 4) {
            return false;
        }
        if (isOverdue(records)) {
            return false;
        }
        BorrowRecord newRecord = new BorrowRecord(UUID.randomUUID(), idBook, customerId, LocalDate.now(), BORROW_TIME,
                false);
        return connector.addBorrowRecord(newRecord);
    }

    private static boolean isOverdue(List<BorrowRecord> records) {
        int overdueSum = 0;
        for (BorrowRecord r : records) {
            overdueSum += calculateOverdue(r);
        }
        return overdueSum == 0;
    }

    private static int calculateOverdue(BorrowRecord record) {
        LocalDate dateBorrowed = record.dateBorrowed();
        Period length = record.duration();
        LocalDate dueDate = dateBorrowed.plus(length);
        LocalDate today = LocalDate.now();
        if (today.isAfter(dueDate)) {
            Period overduePeriod = Period.between(dueDate, today);
            return overduePeriod.getDays();
        }
        return 0;
    }

    // return book, check if user has fine on this book
    public int returnBook(UUID userId, int idBook) {
        RecordFilter filter = new RecordFilter.Builder().idBook(idBook).idCustomer(userId).build();
        List<BorrowRecord> records = this.connector.getRecords(filter);
        if (records.isEmpty()) {
            return 0;
        }
        BorrowRecord record = records.getFirst();
        connector.updateBorrowRecord(new BorrowRecord(record.id(), record.idBook(), record.idCustomer(),
                record.dateBorrowed(), record.duration(), true));
        return calculateOverdue(record) * FRANCS_OVERDUE_PER_DAY;
    }
}
