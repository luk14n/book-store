package com.lukian.bookstore.dto.user;

import com.lukian.bookstore.validation.email.EmailConstraint;
import com.lukian.bookstore.validation.password.PasswordConstraint;

public record UserLoginRequestDto(
        @EmailConstraint String email,
        @PasswordConstraint String password
) {}
