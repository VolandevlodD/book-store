package org.example.bookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.orderitem.OrderItemDto;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Named("fromOrderItemsToDtos")
    default Set<OrderItemDto> toDtos(Set<OrderItem> items) {
        return items.stream().map(this::toDto).collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem toOrderItemFromCartItem(CartItem cartItem);
}
