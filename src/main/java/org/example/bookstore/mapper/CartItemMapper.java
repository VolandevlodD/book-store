package org.example.bookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.cartitem.CartItemDto;
import org.example.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "bookTitle", source = "book", qualifiedByName = "bookTitleExtractor")
    @Mapping(target = "bookId", source = "book", qualifiedByName = "bookIdExtractor")
    CartItemDto toDto(CartItem cartItem);

    @Named("fromCartItemsToDtos")
    default Set<CartItemDto> toDtos(Set<CartItem> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}