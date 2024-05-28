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
import java.util.List;
import javax.sql.DataSource;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    public static final String DELETE_DATA_FROM_DB = "sql/delete-data-from-db.sql";
    public static final String INSERT_DATA_INTO_DB = "sql/insert-data-into-db.sql";

    protected static MockMvc mockMvc;

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Historical Fiction";
    private static final String CATEGORY_DESCRIPTION =
            "A genre of fiction that takes place in the past.";
    private static final Long INVALID_CATEGORY_ID = 999L;
    private static final Long EXPECTED_CATEGORY_ID = 6L;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws Exception {
        try (var connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(DELETE_DATA_FROM_DB));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(INSERT_DATA_INTO_DB));
        }
    }

    @WithMockUser(username = "user")
    @Test
    void getCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, CategoryDto.class));

        assertNotNull(actual);
        assertEquals(5, actual.size());
    }

    @WithMockUser(username = "user")
    @Test
    void getCategoryById() throws Exception {
        CategoryDto expected = createCategoryDto();

        MvcResult result = mockMvc.perform(get("/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createCategory() throws Exception {
        CreateCategoryRequestDto requestDto = createRequestDto();
        CategoryDto expected = createExpectedCategoryDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCategoryById() throws Exception {
        CreateCategoryRequestDto requestDto = createRequestDto();
        requestDto = new CreateCategoryRequestDto("Updated Name", requestDto.description());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", CATEGORY_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertNotNull(actual);
        assertEquals("Updated Name", actual.name());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteCategoryById() throws Exception {
        mockMvc.perform(delete("/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user")
    @Test
    void getBooksByCategory() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, BookDtoWithoutCategoryIds.class));

        assertNotNull(actual);
    }

    private CategoryDto createCategoryDto() {
        return new CategoryDto(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);
    }

    private CategoryDto createExpectedCategoryDto() {
        return new CategoryDto(EXPECTED_CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);
    }

    private CreateCategoryRequestDto createRequestDto() {
        return new CreateCategoryRequestDto(CATEGORY_NAME, CATEGORY_DESCRIPTION);
    }
}
