package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.model.Book;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDtoWithoutCategoryIds> findAll(Pageable pageable);

    BookDto getBookDtoById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);

    Book findBookById(Long id);
}
