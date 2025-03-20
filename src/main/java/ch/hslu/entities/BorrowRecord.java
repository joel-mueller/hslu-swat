package ch.hslu.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public record BorrowRecord(UUID id, int idBook, int idCustomer, LocalDate dateBorrowed, Period duration,
        boolean returned) {

}
