package org.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequestDto(
        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
