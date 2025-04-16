package ch.hslu.entities;

import ch.hslu.persistence.RecordFilter;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BorrowRecord record))
            return false;
        return Objects.equals(id, record.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static int calculateDaysOverdue(BorrowRecord record) {
        LocalDate dateBorrowed = record.getDateBorrowed();
        LocalDate dueDate = dateBorrowed.plus(record.getDuration());
        LocalDate today = LocalDate.now();
        if (today.isAfter(dueDate)) {
            return (int) ChronoUnit.DAYS.between(dueDate, today);
        }
        return 0;
    }

    public boolean filter(RecordFilter filter) {
        if (filter.getId() != null && !Objects.equals(filter.getId(), this.id))
            return false;
        if (filter.getIdBook() != null && !Objects.equals(filter.getIdBook(), this.bookId))
            return false;
        if (filter.getIdCustomer() != null && !Objects.equals(filter.getIdCustomer(), this.customerId))
            return false;
        if (filter.getDateBorrowedBefore() != null && this.dateBorrowed.isAfter(filter.getDateBorrowedBefore()))
            return false;
        if (filter.getDateBorrowedAfter() != null && this.dateBorrowed.isBefore(filter.getDateBorrowedAfter()))
            return false;
        if (filter.getLongerThen() != null && this.duration.minus(filter.getLongerThen()).getDays() > 0)
            return false;
        if (filter.getShorterThen() != null && this.duration.minus(filter.getShorterThen()).getDays() < 0)
            return false;
        return true;
    }

}
