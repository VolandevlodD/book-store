package org.example.bookstore.repository;

import java.util.Optional;
import org.example.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.items it "
            + "LEFT JOIN FETCH it.book "
            + "WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserIdWithItemsWithBooks(@Param("userId") Long userId);
}
