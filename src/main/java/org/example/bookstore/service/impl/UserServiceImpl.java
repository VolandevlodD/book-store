package org.example.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.CreateUserRequestDto;
import org.example.bookstore.dto.user.UserDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.ShoppingCartService;
import org.example.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserDto register(CreateUserRequestDto requestDto) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException(
                    "User with email %s already exist!".formatted(requestDto.email()));
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository
                .findByRole(Role.RoleName.USER)
                .orElseThrow(() ->
                        new RegistrationException("Can't register user, unable to find role"));
        user.setRoles(Set.of(role));
        User dbUser = userRepository.save(user);
        shoppingCartService.createShoppingCart(dbUser);
        return userMapper.toDto(dbUser);
    }
}
