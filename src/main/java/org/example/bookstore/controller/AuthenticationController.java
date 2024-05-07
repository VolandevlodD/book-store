package org.example.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.CreateUserRequestDto;
import org.example.bookstore.dto.UserDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid CreateUserRequestDto createUserRequestDto)
            throws RegistrationException {
        return userService.save(createUserRequestDto);
    }
}
