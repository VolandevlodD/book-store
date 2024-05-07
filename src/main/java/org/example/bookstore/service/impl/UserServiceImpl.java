package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.CreateUserRequestDto;
import org.example.bookstore.dto.UserDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto save(CreateUserRequestDto createUserRequestDto) throws RegistrationException {
        if (userRepository.existsByEmail(createUserRequestDto.email())) {
            throw new RegistrationException(
                    "User with email %s already exist!".formatted(createUserRequestDto.email()));
        }
        User user = userRepository.save(userMapper.toModel(createUserRequestDto));
        return userMapper.toDto(user);
    }
}
