package org.example.bookstore.dto.cartitem;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        Integer quantity
) {
}
