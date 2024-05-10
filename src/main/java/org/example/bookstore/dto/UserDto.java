package org.example.bookstore.dto;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String shippingAddress
) {
}
