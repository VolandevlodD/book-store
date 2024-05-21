package org.example.bookstore.dto.order;

import org.example.bookstore.model.Order;

public record UpdateOrderStatusRequestDto(
        Order.Status status
) {
}
