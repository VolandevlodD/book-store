package org.example.bookstore.dto.shoppingcart;

import java.util.Set;
import org.example.bookstore.dto.cartitem.CartItemDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
}
