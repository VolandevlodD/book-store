package org.example.bookstore.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookRepository.save(bookMapper.toModel(requestDto));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    @Override
    public BookDto findById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookMapper::toDto).orElseThrow(() -> new EntityNotFoundException(
                "Book with id: %d not found".formatted(id)));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        checkIfBookExists(id);
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        checkIfBookExists(id);
        bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Book with %d not found".formatted(id)));
        bookRepository.deleteById(id);
    }

    private void checkIfBookExists(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(id));
        }
    }
}
