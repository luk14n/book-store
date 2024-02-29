package com.lukian.bookstore.dto.order;

import com.lukian.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Order.Status status
) {}
