package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.lukian.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
@Component
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    /**
     * Note:
     * there is no need to map the cart to a specific user,
     * since it's created during the registration process.
     * thus, given cart will be bound to the corresponding user all the time.
     */
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCart toModel(ShoppingCartDto shoppingCartResponseDto);
}
