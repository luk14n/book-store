package com.lukian.bookstore.service.cart;

import com.lukian.bookstore.dto.shoppingcart.AddItemToCartRequestDto;
import com.lukian.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.lukian.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;

public interface ShoppingCartService {

    ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto);

    ShoppingCartDto getCartByUserId(Long userId);

    ShoppingCartDto update(Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto);

    ShoppingCartDto delete(Long userId, Long cartItemId);
}
