package org.example.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank(message = "Shipping address cannot be blank")
        String shippingAddress
) {
}
