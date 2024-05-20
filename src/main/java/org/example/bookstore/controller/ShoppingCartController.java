package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cartitem.CartItemDto;
import org.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import org.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.CartItemService;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping cart management", description = "Endpoints to manage shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @Operation(summary = "Get the shopping cart", description = "Retrieve the shopping cart for "
            + "the authenticated user")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return shoppingCartService.getShoppingCartDtoByUserId(userId);
    }

    @Operation(summary = "Add an item to the cart", description = "Add a new item to the shopping"
            + " cart for the authenticated user")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public CartItemDto addToCart(Authentication authentication,
                                 @RequestBody CreateCartItemRequestDto requestDto) {
        Long userId = getUserIdFromAuthentication(authentication);
        return cartItemService.save(requestDto, userId);
    }

    @Operation(summary = "Update a cart item", description = "Update an existing item in the "
            + "shopping cart for the authenticated user")
    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public CartItemDto updateCart(@RequestBody UpdateCartItemRequestDto requestDto,
                                  @PathVariable Long cartItemId) {
        return cartItemService.update(requestDto, cartItemId);
    }

    @Operation(summary = "Delete a cart item", description = "Remove an item from the shopping "
            + "cart for the authenticated user")
    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteById(cartItemId);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return principal.getId();
    }
}
