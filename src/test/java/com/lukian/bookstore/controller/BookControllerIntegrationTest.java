package com.lukian.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.model.Category;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class BookControllerIntegrationTest {
    private static final String INSERT_BOOKS_SCRIPT =
            "database/books/insert-books.sql";
    private static final String INSERT_CATEGORIES_SCRIPT =
            "database/categories/insert-categories.sql";
    private static final String CLEAN_UP_ALL_DATA_SCRIPT =
            "database/clean-up-data.sql";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_CATEGORIES_SCRIPT));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_SCRIPT));
        }
    }

    @Test
    @DisplayName("Create book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void create_CreateBookWithValidData_ShouldReturnBookDto() throws Exception {
        Long testId = 4L;
        String testTitle = "testTileValid";
        String testAuthor = "testAuthorValid";
        String testIsbn = "testIsbnValid";
        List<Long> testCategoryIds = List.of(1L, 2L);
        BigDecimal testPrice = BigDecimal.valueOf(3.99);
        String testDescription = "Test description";

        BookDto expectedDto = createExpectedBookDto(
                testId, testTitle, testAuthor, testIsbn, testCategoryIds, testPrice);
        CreateBookRequestDto requestDto = createCreateBookRequestDto(
                testTitle, testAuthor, testIsbn, testCategoryIds, testPrice, testDescription);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertBookDto(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Update book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void update_UpdateBookWithValidData_ShouldReturnUpdateBookDto() throws Exception {
        Long testId = 1L;
        String updateTitle = "updateTile";
        String updateAuthor = "updateAuthor";
        String updateIsbn = "updateIsbn";
        List<Long> updateCategoryIds = List.of(1L);
        BigDecimal updatePrice = BigDecimal.valueOf(3.99);
        String updateDescription = "Test description";

        BookDto expectedDto = createExpectedBookDto(
                testId, updateTitle, updateAuthor, updateIsbn, updateCategoryIds, updatePrice);
        CreateBookRequestDto requestDto = createCreateBookRequestDto(
                updateTitle, updateAuthor, updateIsbn,
                updateCategoryIds, updatePrice, updateDescription);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/api/books/{id}", testId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertBookDto(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteById_DeleteBookById_ShouldReturnNoContent() throws Exception {
        Long testId = 1L;

        MvcResult result = mockMvc.perform(delete("/api/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Get book by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void findById_GetBookByValidId_ShouldReturnBookDto() throws Exception {
        Long testId = 1L;
        BookDto expectedDto = getBookDto(testId);

        MvcResult result = mockMvc.perform(get("/api/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertBookDto(expectedDto, resultDto);
    }

    @NotNull
    private static BookDto getBookDto(Long testId) {
        String testTitle = "TestBookTitle1";
        String testAuthor = "TestBookAuthor1";
        String testIsbn = "TestBookIsbn1";
        List<Long> testCategoryIds = List.of(1L);
        BigDecimal testPrice = BigDecimal.valueOf(100);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(testId);
        expectedDto.setTitle(testTitle);
        expectedDto.setAuthor(testAuthor);
        expectedDto.setIsbn(testIsbn);
        expectedDto.setCategoriesIds(testCategoryIds);
        expectedDto.setPrice(testPrice);
        return expectedDto;
    }

    private CreateBookRequestDto createCreateBookRequestDto(
            String title, String author, String isbn,
            List<Long> categoryIds, BigDecimal price, String description) {

        Set<Category> categories = categoryIds.stream()
                .map(Category::new)
                .collect(Collectors.toSet());

        return new CreateBookRequestDto(
                title,
                author,
                isbn,
                price,
                description,
                null,
                categories
        );
    }

    private BookDto createExpectedBookDto(
            Long id, String title, String author, String isbn,
            List<Long> categoryIds, BigDecimal price) {
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        expectedDto.setTitle(title);
        expectedDto.setAuthor(author);
        expectedDto.setIsbn(isbn);
        expectedDto.setCategoriesIds(categoryIds);
        expectedDto.setPrice(price);
        return expectedDto;
    }

    private BookDto createExpectedBookDto(
            String title, String author, String isbn, List<Long> categoryIds, BigDecimal price) {
        return createExpectedBookDto(null, title, author, isbn, categoryIds, price);
    }

    private void assertBookDto(BookDto expected, BookDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCategoriesIds(), actual.getCategoriesIds());
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(CLEAN_UP_ALL_DATA_SCRIPT));
        }
    }
}
