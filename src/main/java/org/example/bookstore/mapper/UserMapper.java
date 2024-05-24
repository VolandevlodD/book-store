package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.user.CreateUserRequestDto;
import org.example.bookstore.dto.user.UserDto;
import org.example.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toModel(CreateUserRequestDto createUserRequestDto);

    @Named("userIdExtractor")
    default Long extractUserId(User user) {
        return user != null ? user.getId() : null;
    }
}
