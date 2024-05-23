package org.example.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.example.bookstore.dto.orderitem.OrderItemDto;
import org.example.bookstore.exception.EmptyCartException;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.OrderItemMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.OrderItemRepository;
import org.example.bookstore.repository.OrderRepository;
import org.example.bookstore.service.OrderService;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Set<OrderDto> getAll(Long userId) {
        return orderRepository.findByCustomerIdWithItems(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderRequestDto orderRequest, Long userId) {
        Set<CartItem> cartItems = cartItemRepository.findByCartId(userId);
        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cannot create order with an empty cart");
        }
        Order newOrder = createAndSaveOrder(orderRequest, userId, cartItems);
        Set<OrderItem> orderItems = createAndSaveOrderItems(cartItems, newOrder);
        newOrder.setOrderItems(orderItems);
        shoppingCartService.clearShoppingCart(userId);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order =
                orderRepository.findByOrderIdWithItems(orderId)
                        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(requestDto.status());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Set<OrderItemDto> getOrderItems(Long orderId, Long userId) {
        Order order =
                orderRepository.findByOrderIdWithItems(orderId)
                        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        checkIfOrderBelongsToUser(order, userId);
        return orderItemMapper.toDtos(order.getOrderItems());
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
        if (!orderItem.getOrder().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this item");
        }
        return orderItemMapper.toDto(orderItem);
    }

    private Set<OrderItem> createAndSaveOrderItems(Set<CartItem> cartItems, Order newOrder) {
        Set<OrderItem> orderItems = cartItems.stream()
                .map(orderItemMapper::toOrderItemFromCartItem)
                .collect(Collectors.toSet());
        orderItems.forEach(orderItem -> orderItem.setOrder(newOrder));
        return orderItems;
    }

    private void checkIfOrderBelongsToUser(Order order, Long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to view this order");
        }
    }

    private Order createAndSaveOrder(CreateOrderRequestDto orderRequest, Long userId,
                                     Set<CartItem> cartItems) {
        Order newOrder = new Order();
        newOrder.setStatus(Order.Status.PENDING);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setShippingAddress(orderRequest.shippingAddress());
        User user = new User();
        user.setId(userId);
        newOrder.setUser(user);
        newOrder.setTotal(calculateTotalPrice(cartItems));
        return orderRepository.save(newOrder);
    }

    private BigDecimal calculateTotalPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
