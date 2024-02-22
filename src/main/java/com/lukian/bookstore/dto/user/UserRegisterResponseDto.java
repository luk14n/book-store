package com.lukian.bookstore.dto.user;

import lombok.Data;

@Data
public class UserRegisterResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
