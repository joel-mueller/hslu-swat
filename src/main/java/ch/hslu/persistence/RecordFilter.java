package ch.hslu.persistence;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class RecordFilter {
    private final UUID id;
    private final Integer idBook;
    private final UUID idCustomer;
    private final LocalDate dateBorrowedBefore;
    private final LocalDate dateBorrowedAfter;
    private final Period longerThen;
    private final Period shorterThen;
    private final Boolean returned;

    private RecordFilter(Builder builder) {
        this.id = builder.id;
        this.idBook = builder.idBook;
        this.idCustomer = builder.idCustomer;
        this.dateBorrowedBefore = builder.dateBorrowedBefore;
        this.dateBorrowedAfter = builder.dateBorrowedAfter;
        this.shorterThen = builder.shorterThen;
        this.longerThen = builder.longerThen;
        this.returned = builder.returned;
    }

    public static class Builder {
        private UUID id;
        private Integer idBook;
        private UUID idCustomer;
        private LocalDate dateBorrowedBefore;
        private LocalDate dateBorrowedAfter;
        private Period longerThen;
        private Period shorterThen;
        private Boolean returned;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder idBook(Integer idBook) {
            this.idBook = idBook;
            return this;
        }

        public Builder idCustomer(UUID idCustomer) {
            this.idCustomer = idCustomer;
            return this;
        }

        public Builder dateBorrowedBefore(LocalDate dateBorrowed) {
            this.dateBorrowedBefore = dateBorrowed;
            return this;
        }

        public Builder duration(Period duration) {
            this.shorterThen = duration;
            return this;
        }

        public Builder returned(Boolean returned) {
            this.returned = returned;
            return this;
        }

        public RecordFilter build() {
            return new RecordFilter(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public UUID getIdCustomer() {
        return idCustomer;
    }

    public LocalDate getDateBorrowedBefore() {
        return dateBorrowedBefore;
    }

    public Period getShorterThen() {
        return shorterThen;
    }

    public Boolean getReturned() {
        return returned;
    }

    public LocalDate getDateBorrowedAfter() {
        return dateBorrowedAfter;
    }

    public Period getLongerThen() {
        return longerThen;
    }

    @Override
    public String toString() {
        return "RecordFilter{" + "id=" + id + ", bookId=" + idBook + ", customerId=" + idCustomer + ", dateBorrowed="
                + dateBorrowedBefore + ", duration=" + shorterThen + ", returned=" + returned + '}';
    }
}
