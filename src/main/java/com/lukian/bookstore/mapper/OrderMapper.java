package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.order.OrderDto;
import com.lukian.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "orderDate", source = "orderDate")
    @Mapping(target = "total", source = "total")
    @Mapping(target = "status", source = "status")
    OrderDto toDto(Order order);
}
