package org.example.bookstore.service;

import java.util.Set;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.example.bookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    Set<OrderDto> getAll(Long userId);

    OrderDto createOrder(CreateOrderRequestDto requestDto, Long userId);

    OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

    Set<OrderItemDto> getOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId);
}
