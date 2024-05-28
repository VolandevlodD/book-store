package org.example.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private static final Long BOOK_ID = 1L;
    private static final String AUTHOR = "Author";
    private static final String ISBN = "ISBN";
    private static final BigDecimal PRICE = BigDecimal.ONE;
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String COVER_IMAGE = "Cover image";
    private static final Set<Long> CATEGORY_IDS = Set.of(1L);
    private static final Long CATEGORY_ID = 1L;
    private static final String UPDATED_AUTHOR = "Updated Author";
    private static final String UPDATED_ISBN = "Updated ISBN";
    private static final BigDecimal UPDATED_PRICE = BigDecimal.TEN;
    private static final String UPDATED_DESCRIPTION = "Updated description";
    private static final String UPDATED_COVER_IMAGE = "Updated cover image";
    private static final String EXPECTED_BOOK_NOT_FOUND_MESSAGE =
            "Book with id: %d not found".formatted(BOOK_ID);
    private static final String EXPECTED_CATEGORY_NOT_FOUND_MESSAGE =
            "Category with id: %d not found".formatted(CATEGORY_ID);

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto requestDto;
    private Book book;

    private static BookDto getExpectedBookDto() {
        BookDto expected = new BookDto();
        expected.setId(BOOK_ID);
        expected.setAuthor(UPDATED_AUTHOR);
        expected.setIsbn(UPDATED_ISBN);
        expected.setPrice(UPDATED_PRICE);
        expected.setTitle(TITLE);
        expected.setDescription(UPDATED_DESCRIPTION);
        expected.setCoverImage(UPDATED_COVER_IMAGE);
        expected.setCategoryIds(CATEGORY_IDS);
        return expected;
    }

    @BeforeEach
    void setUp() {
        requestDto = createRequestDto();
        book = createBook();
    }

    @Test
    @DisplayName("Save valid book")
    void save_ValidBook_ReturnsValidBookDto() {
        BookDto expected = new BookDto();
        expected.setId(BOOK_ID);
        expected.setAuthor(AUTHOR);
        expected.setIsbn(ISBN);
        expected.setPrice(PRICE);
        expected.setTitle(TITLE);
        expected.setDescription(DESCRIPTION);
        expected.setCoverImage(COVER_IMAGE);
        expected.setCategoryIds(CATEGORY_IDS);

        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);
        doNothing().when(categoryService).checkIfCategoriesExistsByIds(anySet());

        BookDto actual = bookService.save(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save book with invalid category id")
    void save_InvalidCategoryId_ThrowsException() {
        doThrow(new EntityNotFoundException(EXPECTED_CATEGORY_NOT_FOUND_MESSAGE))
                .when(categoryService).checkIfCategoriesExistsByIds(anySet());

        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class, () -> bookService.save(requestDto));

        assertEquals(EXPECTED_CATEGORY_NOT_FOUND_MESSAGE, thrown.getMessage());
    }

    @Test
    @DisplayName("Find all books with default pageable params")
    void findAll_DefaultPageableParams_ReturnsAllBookDtos() {
        Page<Book> bookPage = new PageImpl<>(List.of(book, book));
        Pageable defaultPageable = PageRequest.of(0, 20);
        when(bookRepository.findAll(defaultPageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategoryIds(any(Book.class))).thenReturn(
                toBookDtoWithoutCategoryIds(book));

        List<BookDtoWithoutCategoryIds> response = bookService.findAll(defaultPageable);

        assertEquals(bookPage.getSize(), response.size());
        verify(bookRepository).findAll(defaultPageable);
        verify(bookMapper, times(bookPage.getSize())).toDtoWithoutCategoryIds(any(Book.class));
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Get existing book by ID")
    void getBookDtoById_ExistingId_ReturnsBookDto() {
        BookDto expected = new BookDto();
        expected.setId(BOOK_ID);
        expected.setAuthor(AUTHOR);
        expected.setIsbn(ISBN);
        expected.setPrice(PRICE);
        expected.setTitle(TITLE);
        expected.setDescription(DESCRIPTION);
        expected.setCoverImage(COVER_IMAGE);
        expected.setCategoryIds(CATEGORY_IDS);

        when(bookRepository.findByIdWithCategories(BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);

        BookDto actual = bookService.getBookDtoById(BOOK_ID);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get non-existing book by ID")
    void getBookDtoById_NonExistingId_ThrowsException() {
        when(bookRepository.findByIdWithCategories(BOOK_ID)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookDtoById(BOOK_ID));

        assertEquals(EXPECTED_BOOK_NOT_FOUND_MESSAGE, thrown.getMessage());
    }

    @Test
    @DisplayName("Update book with valid data")
    void update_ValidData_Success() {
        Book updatedBook = createUpdatedBook();
        BookDto expected = getExpectedBookDto();

        when(bookRepository.existsById(BOOK_ID)).thenReturn(true);
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(updatedBook);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);

        BookDto actual = bookService.update(BOOK_ID, requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update book with invalid ID")
    void update_InvalidId_ThrowsException() {
        when(bookRepository.existsById(BOOK_ID)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(BOOK_ID, requestDto));

        assertEquals(EXPECTED_BOOK_NOT_FOUND_MESSAGE, thrown.getMessage());
    }

    @Test
    @DisplayName("Delete book by valid ID")
    void deleteById_ValidId_Success() {
        when(bookRepository.existsById(BOOK_ID)).thenReturn(true);

        bookService.deleteById(BOOK_ID);

        verify(bookRepository).deleteById(BOOK_ID);
    }

    @Test
    @DisplayName("Delete book by invalid ID")
    void deleteById_InvalidId_ThrowsException() {
        when(bookRepository.existsById(BOOK_ID)).thenReturn(false);

        EntityNotFoundException thrown =
                assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(BOOK_ID));

        assertEquals(EXPECTED_BOOK_NOT_FOUND_MESSAGE, thrown.getMessage());
    }

    @Test
    @DisplayName("Find all books by category ID")
    void findAllByCategoryId_ValidCategoryId_Success() {
        List<Book> books = List.of(book, book);
        when(bookRepository.findAllByCategoryId(CATEGORY_ID, Pageable.unpaged())).thenReturn(books);
        when(bookMapper.toDtoWithoutCategoryIds(any(Book.class))).thenReturn(
                toBookDtoWithoutCategoryIds(book));

        List<BookDtoWithoutCategoryIds> response =
                bookService.findAllByCategoryId(CATEGORY_ID, Pageable.unpaged());

        assertEquals(books.size(), response.size());
        verify(categoryService).checkIfCategoryExistsById(CATEGORY_ID);
        verify(bookRepository).findAllByCategoryId(CATEGORY_ID, Pageable.unpaged());
        verify(bookMapper, times(books.size())).toDtoWithoutCategoryIds(any(Book.class));
    }

    @Test
    @DisplayName("Find all books by category ID with invalid category ID")
    void findAllByCategoryId_InvalidCategoryId_ThrowsException() {
        doThrow(new EntityNotFoundException(EXPECTED_CATEGORY_NOT_FOUND_MESSAGE)).when(
                categoryService).checkIfCategoryExistsById(anyLong());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> bookService.findAllByCategoryId(CATEGORY_ID, Pageable.unpaged()));

        assertEquals(EXPECTED_CATEGORY_NOT_FOUND_MESSAGE, thrown.getMessage());
    }

    private CreateBookRequestDto createRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(TITLE);
        dto.setAuthor(AUTHOR);
        dto.setIsbn(ISBN);
        dto.setPrice(PRICE);
        dto.setDescription(DESCRIPTION);
        dto.setCoverImage(COVER_IMAGE);
        dto.setCategoryIds(CATEGORY_IDS);
        return dto;
    }

    private Book createBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(TITLE);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);
        book.setPrice(PRICE);
        book.setDescription(DESCRIPTION);
        book.setCoverImage(COVER_IMAGE);
        book.setCategories(Set.of(new Category(CATEGORY_ID)));
        return book;
    }

    private Book createUpdatedBook() {
        Book updatedBook = new Book();
        updatedBook.setId(BOOK_ID);
        updatedBook.setTitle(TITLE);
        updatedBook.setAuthor(UPDATED_AUTHOR);
        updatedBook.setIsbn(UPDATED_ISBN);
        updatedBook.setPrice(UPDATED_PRICE);
        updatedBook.setDescription(UPDATED_DESCRIPTION);
        updatedBook.setCoverImage(UPDATED_COVER_IMAGE);
        updatedBook.setCategories(Set.of(new Category(CATEGORY_ID)));
        return updatedBook;
    }

    private BookDtoWithoutCategoryIds toBookDtoWithoutCategoryIds(Book book) {
        return new BookDtoWithoutCategoryIds(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage()
        );
    }
}
