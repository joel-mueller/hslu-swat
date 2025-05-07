package ch.hslu.dto;

import java.util.UUID;

public record BorrowBookRequest(String customerId, int bookId) {

}
