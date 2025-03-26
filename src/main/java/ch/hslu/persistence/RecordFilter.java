package ch.hslu.persistence;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class RecordFilter {
    private final UUID id;
    private final Integer idBook;
    private final UUID idCustomer;
    private final LocalDate dateBorrowed;
    private final Period duration;
    private final Boolean returned;

    private RecordFilter(Builder builder) {
        this.id = builder.id;
        this.idBook = builder.idBook;
        this.idCustomer = builder.idCustomer;
        this.dateBorrowed = builder.dateBorrowed;
        this.duration = builder.duration;
        this.returned = builder.returned;
    }

    public static class Builder {
        private UUID id;
        private Integer idBook;
        private UUID idCustomer;
        private LocalDate dateBorrowed;
        private Period duration;
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

        public Builder dateBorrowed(LocalDate dateBorrowed) {
            this.dateBorrowed = dateBorrowed;
            return this;
        }

        public Builder duration(Period duration) {
            this.duration = duration;
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

    @Override
    public String toString() {
        return "RecordFilter{" + "id=" + id + ", bookId=" + idBook + ", customerId=" + idCustomer + ", dateBorrowed="
                + dateBorrowed + ", duration=" + duration + ", returned=" + returned + '}';
    }
}
