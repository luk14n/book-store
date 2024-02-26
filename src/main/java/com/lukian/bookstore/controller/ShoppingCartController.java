package com.lukian.bookstore.controller;

import com.lukian.bookstore.dto.shoppingcart.AddItemToCartRequestDto;
import com.lukian.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.lukian.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.service.cart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Endpoints for shopping cart operations")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    /**
     * Retrieving user from request header (Authentication obj.)
     * in order to work only with particular user, his cart, orders, etc.
     * Principal represents the authenticated user
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add books to the shopping cart",
            description = "Add books to the user's shopping cart")
    ShoppingCartDto addBooks(Authentication authentication,
                             @RequestBody @Valid AddItemToCartRequestDto requestDto) {

        User user = (User) authentication.getPrincipal();
        return cartService.addBooksToCartByUserId(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get the shopping cart",
            description = "Get the user's current shopping cart and display what's in it")
    ShoppingCartDto getCart(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return cartService.getCartByUserId(user.getId());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update a cart item",
            description = "Update a specific item in the shopping cart")
    ShoppingCartDto updateItems(Authentication authentication,
                                @PathVariable Long cartItemId,
                                @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return cartService.update(user.getId(), cartItemId, requestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete a cart item",
            description = "Delete a specific item from the shopping cart")
    ShoppingCartDto deleteItems(Authentication authentication,
                                @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        return cartService.delete(user.getId(), cartItemId);
    }
}
