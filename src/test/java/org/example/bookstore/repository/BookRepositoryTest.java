package org.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    public static final String DELETE_DATA_FROM_DB = "classpath:sql/delete-data-from-db.sql";
    public static final String INSERT_DATA_INTO_DB = "classpath:sql/insert-data-into-db.sql";
    private static final Long BOOK_ID = 1L;
    private static final String AUTHOR = "Leo Tolstoy";
    private static final String ISBN = "978-0-1404-4850-4";
    private static final BigDecimal PRICE = BigDecimal.valueOf(19.99);
    private static final String TITLE = "War and Peace";
    private static final String DESCRIPTION =
            "A detailed description of the history of the Napoleonic wars.";
    private static final String COVER_IMAGE = "cover-image-url-1";
    private static final String CATEGORY_NAME = "Historical Fiction";
    private static final Long VALID_CATEGORY_ID = 1L;
    private static final Long INVALID_CATEGORY_ID = 999L;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        book = createBook();
    }

    @Test
    @DisplayName("Find Book by Id with Categories")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByIdWithCategories() {
        Book actual = bookRepository.findByIdWithCategories(book.getId()).get();

        assertNotNull(actual);
        assertEquals(book, actual);
    }

    @Test
    @DisplayName("Find All Books by Category Id with Pagination")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoryId() {
        List<Book> expected = List.of(book);
        List<Book> actual = bookRepository.findAllByCategoryId(
                VALID_CATEGORY_ID, Pageable.unpaged());
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find All Books by Invalid Category Id")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoryIdInvalidCategory() {
        List<Book> actual = bookRepository.findAllByCategoryId(
                INVALID_CATEGORY_ID, Pageable.unpaged());

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName(CATEGORY_NAME);
        category.setDescription(DESCRIPTION);
        return category;
    }

    private Book createBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);
        book.setPrice(PRICE);
        book.setTitle(TITLE);
        book.setDescription(DESCRIPTION);
        book.setCoverImage(COVER_IMAGE);
        book.setCategories(Set.of(createCategory()));
        return book;
    }
}
