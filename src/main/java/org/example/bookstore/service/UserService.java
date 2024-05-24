package org.example.bookstore.service;

import org.example.bookstore.dto.user.CreateUserRequestDto;
import org.example.bookstore.dto.user.UserDto;

public interface UserService {
    UserDto register(CreateUserRequestDto createUserRequestDto);
}
