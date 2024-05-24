package org.example.bookstore.service;

import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCart getShoppingCartByUserId(Long userId);

    ShoppingCartDto getShoppingCartDtoByUserId(Long userId);
}
