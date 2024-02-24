package com.lukian.bookstore.controller;

import com.lukian.bookstore.dto.user.UserLoginRequestDto;
import com.lukian.bookstore.dto.user.UserLoginResponseDto;
import com.lukian.bookstore.dto.user.UserRegisterRequestDto;
import com.lukian.bookstore.dto.user.UserRegisterResponseDto;
import com.lukian.bookstore.exception.RegistrationException;
import com.lukian.bookstore.security.AuthenticationService;
import com.lukian.bookstore.service.user.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication management",
        description = "Endpoints for managing online store authentication processes")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserServiceImpl userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Performs a registration of a new user (i.e. adds a new user to the DB)")
    public UserRegisterResponseDto register(@RequestBody @Valid UserRegisterRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "User Log-in",
            description = "Performs logging in, provided given user was registered before")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
