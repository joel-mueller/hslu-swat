package ch.hslu.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class BorrowRecord {
    private final UUID id;
    private final int bookId;
    private final UUID customerId;
    private final LocalDate dateBorrowed;
    private final Period duration;
    private boolean returned;

    private BorrowRecord(Builder builder) {
        this.id = builder.id;
        this.bookId = builder.bookId;
        this.customerId = builder.customerId;
        this.dateBorrowed = builder.dateBorrowed;
        this.duration = builder.duration;
        this.returned = builder.returned;
    }

    public static class Builder {
        private UUID id = UUID.randomUUID();
        private int bookId;
        private UUID customerId;
        private LocalDate dateBorrowed = LocalDate.now();
        private Period duration = Period.ofDays(30);
        private boolean returned = false;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder bookId(int bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder dateBorrowed(LocalDate dateBorrowed) {
            this.dateBorrowed = dateBorrowed;
            return this;
        }

        public Builder duration(Period duration) {
            this.duration = duration;
            return this;
        }

        public Builder returned(boolean returned) {
            this.returned = returned;
            return this;
        }

        public BorrowRecord build() {
            return new BorrowRecord(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public Period getDuration() {
        return duration;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public static int calculateOverdue(BorrowRecord record) { // rename
        LocalDate dateBorrowed = record.getDateBorrowed();
        Period length = record.getDuration();
        LocalDate dueDate = dateBorrowed.plus(length);
        LocalDate today = LocalDate.now();
        if (today.isAfter(dueDate)) {
            Period overduePeriod = Period.between(dueDate, today);
            return overduePeriod.getDays();
        }
        return 0;
    }
}
