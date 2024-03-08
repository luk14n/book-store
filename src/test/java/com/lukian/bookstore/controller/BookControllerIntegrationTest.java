package com.lukian.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.model.Category;
import com.lukian.bookstore.service.book.BookService;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

    @Autowired
    private BookService bookService;

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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void create_CreateBookWithValidData_ShouldReturnBookDto() throws Exception {
        Category category = new Category(1L);
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "New Book",
                "Author Name",
                "1234567890123",
                new BigDecimal("100.0"),
                "Book Description",
                "Cover Image URL",
                Set.of(category)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value(requestDto.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author")
                        .value(requestDto.author()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn")
                        .value(requestDto.isbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price")
                        .value(requestDto.price().doubleValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value(requestDto.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coverImage")
                        .value(requestDto.coverImage()));
    }

    @Test
    @DisplayName("Delete book by ID as admin")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void delete_DeleteBookByIdAsAdmin_ShouldReturnNoContent() throws Exception {
        Long bookIdToDelete = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", bookIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertThrows(IllegalArgumentException.class,
                () -> bookService.findById(bookIdToDelete));
    }

    @Test
    @DisplayName("Get book by ID as user")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void get_GetBookByIdAsUser_ShouldReturnBookDto() throws Exception {
        Long bookIdToRetrieve = 1L;
        String expectedTitle = "Test Title 1";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", bookIdToRetrieve)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedTitle))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookIdToRetrieve));
    }

    @Test
    @DisplayName("Get all books as user with pagination")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void get_GetAllBooksAsUserWithPagination_ShouldReturnBookDtoList() throws Exception {
        int page = 0;
        int size = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Test Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Test Title 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Test Author 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("Test Title 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].author").value("Test Author 3"));
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
