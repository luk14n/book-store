package com.lukian.bookstore.dto.order;

import com.lukian.bookstore.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Order.Status status
) {}
