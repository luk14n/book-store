package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.order.OrderItemDto;
import com.lukian.bookstore.model.CartItem;
import com.lukian.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItemModel(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemDto toDto(OrderItem orderItem);
}
