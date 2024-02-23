package com.lukian.bookstore.dto.user;

public record UserRegisterResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String shippingAddress
) {}
