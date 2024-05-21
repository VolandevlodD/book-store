package org.example.bookstore.repository;

import java.util.Optional;
import java.util.Set;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o JOIN FETCH o.orderItems WHERE o.user.id = :userId")
    Set<Order> findByCustomerIdWithItems(@Param("userId") Long customerId);

    @Query("FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findByOrderIdWithItems(@Param("orderId") Long orderId);
}
