package org.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final String DELETE_DATA_FROM_DB = "sql/delete-data-from-db.sql";
    public static final String INSERT_DATA_INTO_DB = "sql/insert-data-into-db.sql";

    protected static MockMvc mockMvc;

    private static final Long BOOK_ID = 1L;
    private static final String AUTHOR = "Jane Austen";
    private static final String ISBN = "9780307957566";
    private static final BigDecimal PRICE = BigDecimal.valueOf(15.99);
    private static final String TITLE = "Pride and Prejudice";
    private static final String DESCRIPTION =
            "A classic novel that explores the issues of marriage, money, and social class.";
    private static final String COVER_IMAGE = "cover-image-url-2";
    private static final Set<Long> CATEGORY_IDS = Set.of(2L);
    private static final long RESPONSE_BOOK_ID = 5L;
    private static final String RESPONSE_TITLE = "To Kill a Mockingbird";
    private static final String RESPONSE_AUTHOR = "Harper Lee";
    private static final String RESPONSE_ISBN = "978-0-06-112008-4";
    private static final BigDecimal RESPONSE_PRICE = BigDecimal.valueOf(18.99);
    private static final String RESPONSE_DESCRIPTION =
            "A novel about the serious issues of rape and racial inequality in the American South.";
    private static final String RESPONSE_COVER_IMAGE = "cover-image-url-5";
    private static final Set<Long> RESPONSE_CATEGORY_IDS = Set.of(5L);

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext

    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    private static BookDto createResponseDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(RESPONSE_BOOK_ID);
        bookDto.setAuthor(RESPONSE_AUTHOR);
        bookDto.setIsbn(RESPONSE_ISBN);
        bookDto.setPrice(RESPONSE_PRICE);
        bookDto.setTitle(RESPONSE_TITLE);
        bookDto.setDescription(RESPONSE_DESCRIPTION);
        bookDto.setCategoryIds(RESPONSE_CATEGORY_IDS);
        bookDto.setCoverImage(RESPONSE_COVER_IMAGE);
        return bookDto;
    }

    @BeforeEach
    @SneakyThrows
    void setUp(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(DELETE_DATA_FROM_DB));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_DATA_INTO_DB));
        }
    }

    @WithMockUser(username = "admin")
    @Test
    @SneakyThrows
    void getAll() {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, BookDtoWithoutCategoryIds.class)
        );

        assertNotNull(actual);
        assertEquals(5, actual.size());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void getBookById() {
        BookDto expected = createResponseDto();

        MvcResult result = mockMvc.perform(get("/books/{id}", RESPONSE_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @SneakyThrows
    void createBook() {
        CreateBookRequestDto requestDto = createRequestDto();

        BookDto expected = createBookDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @SneakyThrows
    void updateBookById() {
        CreateBookRequestDto requestDto = createRequestDto();
        requestDto.setTitle("Updated Title");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/{id}", BOOK_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertEquals("Updated Title", actual.getTitle());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @SneakyThrows
    void deleteBookById() {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private BookDto createBookDto() {
        BookDto expected = new BookDto();
        expected.setAuthor(AUTHOR);
        expected.setTitle(TITLE);
        expected.setDescription(DESCRIPTION);
        expected.setPrice(PRICE);
        expected.setCoverImage(COVER_IMAGE);
        expected.setCategoryIds(CATEGORY_IDS);
        expected.setIsbn(ISBN);
        return expected;
    }

    private CreateBookRequestDto createRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(AUTHOR);
        requestDto.setTitle(TITLE);
        requestDto.setDescription(DESCRIPTION);
        requestDto.setPrice(PRICE);
        requestDto.setCoverImage(COVER_IMAGE);
        requestDto.setCategoryIds(CATEGORY_IDS);
        requestDto.setIsbn(ISBN);
        return requestDto;
    }
}
