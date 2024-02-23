package com.lukian.bookstore.service.user;

import com.lukian.bookstore.dto.user.UserRegisterRequestDto;
import com.lukian.bookstore.dto.user.UserRegisterResponseDto;
import com.lukian.bookstore.exception.RegistrationException;

public interface UserService {
    UserRegisterResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException;
}
