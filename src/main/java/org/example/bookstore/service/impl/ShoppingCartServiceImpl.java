package org.example.bookstore.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.ShoppingCartMapper;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.ShoppingCartRepository;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getShoppingCartByUserId(Long userId) {
        Optional<ShoppingCart> cartOptional =
                shoppingCartRepository.findByUserIdWithItemsWithBooks(userId);
        return cartOptional.orElseThrow(() -> new EntityNotFoundException(
                "Shopping cart not found for userId: " + userId));
    }

    @Override
    public ShoppingCartDto getShoppingCartDtoByUserId(Long userId) {
        ShoppingCart cart = getShoppingCartByUserId(userId);
        return shoppingCartMapper.toDto(cart);
    }
}
