package org.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequestDto(
        @NotNull
        @Min(1)
        Integer quantity
) {
}
