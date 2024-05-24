package org.example.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;

@Entity
@Data
@SoftDelete
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "id", referencedColumnName = "id")
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> items;
}

