package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.CreateUserRequestDto;
import org.example.bookstore.dto.user.UserDto;
import org.example.bookstore.dto.user.UserLoginRequestDto;
import org.example.bookstore.dto.user.UserLoginResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.security.AuthenticationService;
import org.example.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid CreateUserRequestDto createUserRequestDto)
            throws RegistrationException {
        return userService.register(createUserRequestDto);
    }

    @Operation(summary = "Login a user")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
