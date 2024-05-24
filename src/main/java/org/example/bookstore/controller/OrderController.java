package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.example.bookstore.dto.orderitem.OrderItemDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management", description = "Endpoints to manage orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get a list of orders", description = "Get a list of orders for current "
            + "user")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Set<OrderDto> getOrders(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return orderService.getAll(userId);
    }

    @Operation(summary = "Add an order", description = "Add a new order")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderDto addOrder(@RequestBody @Valid CreateOrderRequestDto requestDto,
                             Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return orderService.createOrder(requestDto, userId);
    }

    @Operation(summary = "Update an order", description = "Update order status")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateOrderStatus(@PathVariable Long orderId,
                                      @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto);
    }

    @Operation(summary = "Get a list of items in order", description = "Get a list of items for "
            + "current user order")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public Set<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                           Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return orderService.getOrderItems(orderId, userId);
    }

    @Operation(summary = "Get an item details from order", description = "Get an item details "
            + "from a specific order"
            + "current user order")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemDto getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId,
                                          Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return orderService.getOrderItem(orderId, itemId, userId);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return principal.getId();
    }
}
