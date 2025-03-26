package ch.hslu.entities;

import java.util.UUID;

public record Customer(UUID id, String firstName, String lastName, String street, String zipCode) {
}
