package org.example.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@Setter
@SoftDelete
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;

    @JoinColumn(name = "id", referencedColumnName = "id")
    @MapsId
    @OneToOne
    private User user;

    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> items;
}

