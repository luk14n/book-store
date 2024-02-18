package com.lukian.bookstore.service.user;

import com.lukian.bookstore.dto.user.CreateUserRequestDto;
import com.lukian.bookstore.dto.user.UserDto;
import com.lukian.bookstore.exception.RegistrationException;

public interface UserService {
    UserDto register(CreateUserRequestDto requestDto) throws RegistrationException;
}
