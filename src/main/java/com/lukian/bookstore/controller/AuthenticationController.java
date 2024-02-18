package com.lukian.bookstore.controller;

import com.lukian.bookstore.dto.user.CreateUserRequestDto;
import com.lukian.bookstore.dto.user.UserDto;
import com.lukian.bookstore.exception.RegistrationException;
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

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Performs a registration of a new user (i.e. adds a new user to the DB)")
    public UserDto register(@RequestBody @Valid CreateUserRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
