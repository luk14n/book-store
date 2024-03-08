package com.lukian.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukian.bookstore.dto.category.CategoryDto;
import com.lukian.bookstore.dto.category.CreateCategoryRequestDto;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerIntegrationTest {
    private static final String INSERT_BOOKS_SCRIPT =
            "database/books/insert-books.sql";
    private static final String INSERT_CATEGORIES_SCRIPT =
            "database/categories/insert-categories.sql";
    private static final String CLEAN_UP_SCRIPT =
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
                    new ClassPathResource(
                            INSERT_CATEGORIES_SCRIPT));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            INSERT_BOOKS_SCRIPT));
        }
    }

    @Test
    @DisplayName("Create new category as admin")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void create_CreateNewCategoryAsAdmin_ShouldReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "New Category", "Category Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Category"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(
                        "Category Description"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        CategoryDto createdCategory = objectMapper.readValue(content, CategoryDto.class);

        assertNotNull(createdCategory.id());
        assertEquals("New Category", createdCategory.name());
        assertEquals("Category Description", createdCategory.description());
    }

    @Test
    @DisplayName("Get all categories as user with pagination")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void get_GetAllCategoriesAsUserWithPagination_ShouldReturnCategoryDtoList() throws Exception {
        int page = 0;
        int size = 10;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value("Test Category 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                        .value("Test Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name")
                        .value("Test Category 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description")
                        .value("Test Description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name")
                        .value("Test Category 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description")
                        .value("Test Description 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name")
                        .value("Test Category 4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].description")
                        .value("Test Description 4"));
    }

    @Test
    @DisplayName("Get category by ID as user")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void get_GetCategoryByIdAsUser_ShouldReturnCategoryDto() throws Exception {
        Long categoryIdToRetrieve = 1L;
        String name = "Test Category 1";
        String description = "Test Description 1";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{id}", categoryIdToRetrieve)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryIdToRetrieve))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(CLEAN_UP_SCRIPT));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }
}
