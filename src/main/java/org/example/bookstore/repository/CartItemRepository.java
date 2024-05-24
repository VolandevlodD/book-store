package org.example.bookstore.repository;

import java.util.Optional;
import org.example.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem ci JOIN FETCH ci.book WHERE ci.id = :itemId")
    Optional<CartItem> findByIdWithBookAndShoppingCart(@Param("itemId") Long itemId);
}
