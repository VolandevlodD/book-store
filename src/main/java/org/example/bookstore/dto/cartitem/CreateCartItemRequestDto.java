package org.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateCartItemRequestDto(
        @NotNull(message = "bookId can't be null")
        @PositiveOrZero
        Long bookId,
        @NotNull(message = "quantity can't be null")
        @Positive(message = "quantity must be positive")
        Integer quantity
) {
}
