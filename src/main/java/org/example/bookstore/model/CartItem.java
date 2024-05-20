package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
    @ManyToOne
    private ShoppingCart shoppingCart;

    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @ManyToOne
    private Book book;

    @Column(nullable = false)
    private Integer quantity;
}
