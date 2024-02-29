package com.lukian.bookstore.controller;

import com.lukian.bookstore.dto.order.OrderDto;
import com.lukian.bookstore.dto.order.OrderItemDto;
import com.lukian.bookstore.dto.order.PlaceOrderRequestDto;
import com.lukian.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Place a new order",
            description = "Create a new order for the authenticated user")
    OrderDto placeOrder(Authentication authentication,
                        @RequestBody @Valid PlaceOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.save(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all orders",
            description = "Get all orders for the authenticated user")
    List<OrderDto> getAllOrders(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user.getId(), pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status",
            description = "Update the status of a specific order")
    OrderDto updateOrderStatus(Authentication authentication,
                               @RequestBody @Valid UpdateOrderStatusRequestDto requestDto,
                               @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return orderService.update(user.getId(), requestDto, id);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all order items",
            description = "Get all items for a specific order")
    List<OrderItemDto> getAllOrderItems(Authentication authentication,
                                        @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrderItemsByOrderId(user.getId(), orderId);
    }

    @GetMapping("{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get order item by ID",
            description = "Get a specific order item by its ID")
    OrderItemDto getOrderItem(Authentication authentication,
                              @PathVariable Long itemId,
                              @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.finOrderItemById(user.getId(), itemId, orderId);
    }
}
