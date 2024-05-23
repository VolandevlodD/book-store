package org.example.bookstore.service;

import org.example.bookstore.dto.cartitem.CartItemDto;
import org.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import org.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto cartItemDto, Long userId);

    void deleteById(Long id);

    CartItemDto update(UpdateCartItemRequestDto requestDto, Long cartItemId, Long userId);
}
