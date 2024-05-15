package org.example.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.CreateUserRequestDto;
import org.example.bookstore.dto.UserDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto save(CreateUserRequestDto createUserRequestDto) throws RegistrationException {
        if (userRepository.existsByEmail(createUserRequestDto.email())) {
            throw new RegistrationException(
                    "User with email %s already exist!".formatted(createUserRequestDto.email()));
        }
        User user = userMapper.toModel(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository
                .findByRole(Role.RoleName.USER)
                .orElseThrow(() ->
                        new RegistrationException("Can't register user, unable to find role"));
        user.setRoles(Set.of(role));
        User dbUser = userRepository.save(user);
        return userMapper.toDto(dbUser);
    }
}
