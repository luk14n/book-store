package com.lukian.bookstore.controller;

import com.lukian.bookstore.dto.order.OrderDto;
import com.lukian.bookstore.dto.order.OrderItemDto;
import com.lukian.bookstore.dto.order.PlaceOrderRequestDto;
import com.lukian.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.service.order.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    OrderDto placeOrder(Authentication authentication,
                        @RequestBody @Valid PlaceOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.save(user.getId(), requestDto);
    }

    @GetMapping
    List<OrderDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user.getId());
    }

    @PatchMapping("/{id}")
    OrderDto updateOrderStatus(Authentication authentication,
                               @RequestBody @Valid UpdateOrderStatusRequestDto requestDto,
                               @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return orderService.update(user.getId(), requestDto, id);
    }

    @GetMapping("/{orderId}/items")
    List<OrderItemDto> getAllOrderItems(Authentication authentication,
                                        @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrderItemsByOrderId(user.getId(), orderId);
    }

    @GetMapping("{orderId}/items/{itemId}")
    OrderItemDto getOrderItem(Authentication authentication,
                              @PathVariable Long itemId,
                              @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.finOrderItemById(user.getId(), itemId, orderId);
    }
}
