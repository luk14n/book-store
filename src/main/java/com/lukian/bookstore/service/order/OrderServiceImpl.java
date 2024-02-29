package com.lukian.bookstore.service.order;

import com.lukian.bookstore.dto.order.OrderDto;
import com.lukian.bookstore.dto.order.OrderItemDto;
import com.lukian.bookstore.dto.order.PlaceOrderRequestDto;
import com.lukian.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.lukian.bookstore.exception.EntityNotFoundException;
import com.lukian.bookstore.mapper.OrderItemMapper;
import com.lukian.bookstore.mapper.OrderMapper;
import com.lukian.bookstore.model.Order;
import com.lukian.bookstore.model.OrderItem;
import com.lukian.bookstore.model.ShoppingCart;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.repository.cart.ShoppingCartRepository;
import com.lukian.bookstore.repository.order.OrderItemRepository;
import com.lukian.bookstore.repository.order.OrderRepository;
import com.lukian.bookstore.repository.user.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto save(Long userId, PlaceOrderRequestDto requestDto) {
        Order order = new Order();

        ShoppingCart cartFromDB = getCartFromDB(userId);

        order.setOrderItems(cartFromDB.getCartItems().stream()
                .map(orderItemMapper::toOrderItemModel)
                .peek(orderItemRepository::save)
                .collect(Collectors.toSet()));

        order.setUser(userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Cannot find user by user id: " + userId)));

        order.setShippingAddress(requestDto.shippingAddress());

        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        order.setStatus(Order.Status.PENDING);

        order.setOrderDate(LocalDateTime.now());

        cartFromDB.getCartItems().clear();

        orderRepository.save(order);

        return orderMapper.toDto(order);

    }

    @Override
    public List<OrderDto> findAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        return orderFromDb.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto finOrderItemById(Long userId, Long itemId, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        return orderFromDb.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find item by item id: " + itemId));
    }

    @Override
    public OrderDto update(Long userId, UpdateOrderStatusRequestDto requestDto, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        orderFromDb.setStatus(requestDto.status());
        orderRepository.save(orderFromDb);
        return orderMapper.toDto(orderFromDb);
    }

    private ShoppingCart getCartFromDB(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User userFromDb = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "User not found by user id: " + userId));
                    shoppingCart.setUser(userFromDb);
                    return cartRepository.save(shoppingCart);
                });
    }

    private Order getOrderFromDbByUserIdAndOrderId(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find order by user id: " + userId + " and order id: " + orderId));
    }
}
