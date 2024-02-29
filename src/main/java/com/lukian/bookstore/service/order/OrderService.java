package com.lukian.bookstore.service.order;

import com.lukian.bookstore.dto.order.OrderDto;
import com.lukian.bookstore.dto.order.OrderItemDto;
import com.lukian.bookstore.dto.order.PlaceOrderRequestDto;
import com.lukian.bookstore.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(Long userId, PlaceOrderRequestDto requestDto);

    List<OrderDto> findAll(Long userId, Pageable pageable);

    List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto finOrderItemById(Long userId, Long id, Long orderId);

    OrderDto update(Long userId, UpdateOrderStatusRequestDto requestDto, Long orderId);
}

