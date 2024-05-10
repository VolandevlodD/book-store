package org.example.bookstore.service;

import org.example.bookstore.dto.CreateUserRequestDto;
import org.example.bookstore.dto.UserDto;

public interface UserService {
    UserDto save(CreateUserRequestDto createUserRequestDto);
}
