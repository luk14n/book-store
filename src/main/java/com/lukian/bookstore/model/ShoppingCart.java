package com.lukian.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;

@Entity
@Data
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    /**
     * Mapping should be derived from the User P.K.
     * Bc. we have @OneToOne relation
     * and each cart is associated with one and only one user
     */
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * One shopping cart has several items,
     * and an item is part of one shopping cart.
     * Therefore, we established bidirectional @OneToMany
     */
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> cartItems;
}
