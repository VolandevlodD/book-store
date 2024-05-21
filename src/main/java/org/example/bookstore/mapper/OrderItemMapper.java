package org.example.bookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.orderitem.OrderItemDto;
import org.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book", qualifiedByName = "bookIdExtractor")
    OrderItemDto toDto(OrderItem orderItem);

    @Named("fromOrderItemsToDtos")
    default Set<OrderItemDto> toDtos(Set<OrderItem> items) {
        return items.stream().map(this::toDto).collect(Collectors.toSet());
    }
}