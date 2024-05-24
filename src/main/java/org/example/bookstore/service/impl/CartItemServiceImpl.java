package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cartitem.CartItemDto;
import org.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import org.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.service.BookService;
import org.example.bookstore.service.CartItemService;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final BookService bookService;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public CartItemDto save(CreateCartItemRequestDto cartItemDto, Long userId) {
        ShoppingCart cart = shoppingCartService.getShoppingCartByUserId(userId);
        Book book = bookService.findBookById(cartItemDto.bookId());
        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(cartItemDto.quantity());
        item.setShoppingCart(cart);
        CartItem dbCartItem = cartItemRepository.save(item);
        return cartItemMapper.toDto(dbCartItem);
    }

    @Override
    public void deleteById(Long id) {
        checkIfExistsById(id);
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItemDto update(UpdateCartItemRequestDto requestDto, Long cartItemId, Long userId) {
        checkIfExistsById(cartItemId);
        CartItem cartItem = cartItemRepository.findByIdWithBookAndShoppingCart(cartItemId).get();
        if (!cartItem.getShoppingCart().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }
        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    private void checkIfExistsById(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("CartItem with id " + id + " not found");
        }
    }
}
