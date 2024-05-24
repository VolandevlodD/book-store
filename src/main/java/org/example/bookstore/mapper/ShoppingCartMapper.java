package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "items", qualifiedByName = "fromCartItemsToDtos")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
