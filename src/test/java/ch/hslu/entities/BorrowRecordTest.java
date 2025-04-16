package ch.hslu.entities;

import ch.hslu.persistence.RecordFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BorrowRecordTest {

    private BorrowRecord borrowRecord;
    private UUID id;
    private int bookId;
    private UUID customerId;
    private LocalDate dateBorrowed;
    private Period duration;
    private boolean returned;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        bookId = 123;
        customerId = UUID.randomUUID();
        dateBorrowed = LocalDate.of(2024, 4, 1);
        duration = Period.ofDays(30);
        returned = false;

        borrowRecord = new BorrowRecord.Builder().id(id).bookId(bookId).customerId(customerId)
                .dateBorrowed(dateBorrowed).duration(duration).returned(returned).build();
    }

    @Test
    void testBorrowRecordCreation() {
        assertNotNull(borrowRecord);
        assertEquals(id, borrowRecord.getId());
        assertEquals(bookId, borrowRecord.getBookId());
        assertEquals(customerId, borrowRecord.getCustomerId());
        assertEquals(dateBorrowed, borrowRecord.getDateBorrowed());
        assertEquals(duration, borrowRecord.getDuration());
        assertFalse(borrowRecord.isReturned());
    }

    @Test
    void testSetReturned() {
        borrowRecord.setReturned(true);
        assertTrue(borrowRecord.isReturned());
    }

    @Test
    void testDefaultDuration() {
        BorrowRecord defaultDurationRecord = new BorrowRecord.Builder().bookId(bookId).customerId(customerId).build();
        assertEquals(Period.ofDays(30), defaultDurationRecord.getDuration());
    }

    @Test
    void testDefaultDateBorrowed() {
        BorrowRecord defaultDateRecord = new BorrowRecord.Builder().bookId(bookId).customerId(customerId).build();
        assertEquals(LocalDate.now(), defaultDateRecord.getDateBorrowed());
    }

    @Test
    void testBuilderWithCustomValues() {
        LocalDate customDate = LocalDate.of(2025, 4, 1);
        Period customDuration = Period.ofDays(15);

        BorrowRecord customRecord = new BorrowRecord.Builder().id(UUID.randomUUID()).bookId(456)
                .customerId(UUID.randomUUID()).dateBorrowed(customDate).duration(customDuration).returned(true).build();

        assertEquals(customDate, customRecord.getDateBorrowed());
        assertEquals(customDuration, customRecord.getDuration());
        assertTrue(customRecord.isReturned());
    }

    @Test
    void testEqualityOfSameRecords() {
        BorrowRecord identicalRecord = new BorrowRecord.Builder().id(id).build();
        assertEquals(borrowRecord, identicalRecord);
    }

    @Test
    void testHashCodeOfSameRecords() {
        BorrowRecord identicalRecord = new BorrowRecord.Builder().id(id).build();
        assertEquals(borrowRecord.hashCode(), identicalRecord.hashCode());
    }

    @Test
    void testIdUniqueness() {
        BorrowRecord record1 = new BorrowRecord.Builder().bookId(1).customerId(UUID.randomUUID()).build();
        BorrowRecord record2 = new BorrowRecord.Builder().bookId(2).customerId(UUID.randomUUID()).build();
        assertNotEquals(record1.getId(), record2.getId());
    }

    @Test
    void calculateOverdueNotingDaysOverdue() {
        LocalDate dateBorrowed2 = LocalDate.now().minusDays(60);
        Period duration2 = Period.ofDays(60);
        BorrowRecord record2 = new BorrowRecord.Builder().dateBorrowed(dateBorrowed2).duration(duration2).build();
        assertEquals(0, BorrowRecord.calculateDaysOverdue(record2));
    }

    @Test
    void calculateOverdueSomethingDaysOverdue() {
        LocalDate dateBorrowed2 = LocalDate.now().minusDays(77);
        Period duration2 = Period.ofDays(30);
        BorrowRecord record2 = new BorrowRecord.Builder().dateBorrowed(dateBorrowed2).duration(duration2).build();
        assertEquals(47, BorrowRecord.calculateDaysOverdue(record2));
    }

    @Test
    void filter_matchesExactId_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().id(id).build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_nonMatchingId_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().id(UUID.randomUUID()).build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_matchingBookId_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().idBook(bookId).build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_nonMatchingBookId_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().idBook(bookId + 1).build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_matchingCustomerId_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().idCustomer(customerId).build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_nonMatchingCustomerId_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().idCustomer(UUID.randomUUID()).build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_dateBorrowedBefore_match_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().dateBorrowedBefore(LocalDate.of(2024, 5, 1)).build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_dateBorrowedBefore_noMatch_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().dateBorrowedBefore(LocalDate.of(2024, 3, 1)).build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_dateBorrowedAfter_match_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().dateBorrowedAfter(LocalDate.of(2024, 4, 1)) // earlier than
                                                                                                     // fixed
                                                                                                     // dateBorrowed
                .build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_dateBorrowedAfter_noMatch_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().dateBorrowedAfter(LocalDate.of(2024, 4, 15)) // later than
                                                                                                      // fixed
                                                                                                      // dateBorrowed
                .build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_durationShorterThan_match_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().shorterThen(Period.ofDays(20)) // borrowRecord has 30 days
                .build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_durationShorterThan_noMatch_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().shorterThen(Period.ofDays(31)) // less than borrowRecord
                .build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_durationLongerThan_match_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().longerThen(Period.ofDays(29)) // less than borrowRecord
                .build();
        assertFalse(borrowRecord.filter(filter));
    }

    @Test
    void filter_durationLongerThan_noMatch_returnsFalse() {
        RecordFilter filter = new RecordFilter.Builder().longerThen(Period.ofDays(35)) // more than borrowRecord
                .build();
        assertTrue(borrowRecord.filter(filter));
    }

    @Test
    void filter_allFieldsNull_returnsTrue() {
        RecordFilter filter = new RecordFilter.Builder().build();
        assertTrue(borrowRecord.filter(filter));
    }
}
