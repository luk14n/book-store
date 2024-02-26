package com.lukian.bookstore.repository.cart;

import com.lukian.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    /**
     * Retrieves a shopping cart based on the provided user ID.
     * Alongside fetches user, cart items (ShoppingCart field) & book (CartItem field)
     * in order to prevent LIE when initializing cart.
     *
     * @param userId ID of the user whose shopping cart needs to be retrieved.
     * @return Optional (found shopping cart, or an empty optional if not found).
     */
    @Query("SELECT cart FROM ShoppingCart cart "
            + "LEFT JOIN FETCH cart.user "
            + "LEFT JOIN FETCH cart.cartItems items "
            + "LEFT JOIN FETCH items.book "
            + "WHERE :userId = cart.user.id ")
    Optional<ShoppingCart> findByUserId(Long userId);
}
