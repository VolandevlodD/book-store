package org.example.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import org.example.bookstore.model.Order;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Order.Status status
) {
}
