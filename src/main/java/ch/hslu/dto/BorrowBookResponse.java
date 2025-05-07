package ch.hslu.dto;

import java.time.LocalDate;
import java.util.UUID;

public record BorrowBookResponse(UUID customerId, int bookId, LocalDate returnDate) {

}
