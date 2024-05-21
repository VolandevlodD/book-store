package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class, uses = {UserMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user", qualifiedByName = "userIdExtractor")
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "fromOrderItemsToDtos")
    OrderDto toDto(Order order);
}
