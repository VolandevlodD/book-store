package org.example.bookstore.repository;

import java.util.Optional;
import java.util.Set;
import org.example.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem ci JOIN FETCH ci.book WHERE ci.id = :itemId")
    Optional<CartItem> findByIdWithBookAndShoppingCart(@Param("itemId") Long itemId);

    @Query("FROM CartItem ci WHERE ci.shoppingCart.id = :cartId")
    Set<CartItem> findByCartId(@Param("cartId") Long cartId);
}
