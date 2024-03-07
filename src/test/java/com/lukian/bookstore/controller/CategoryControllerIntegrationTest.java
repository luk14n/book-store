package com.lukian.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Create new categories")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void save_SaveNewCategory_ShouldReturnCategoryDto() throws Exception {
        String testName = "TestName";
        String testDescription = "TestDescription";

        CategoryDto expectedDto = createCategoryDto(testName, testDescription);
        CreateCategoryRequestDto requestDto =
                createCategoryRequestDtoDto(testName, testDescription);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(resultDto);
        assertEquals(expectedDto.name(), resultDto.name());
        assertEquals(expectedDto.description(), resultDto.description());
    }

    @Test
    @DisplayName("Update category by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void update_UpdateCategory_ShouldReturnUpdatedCategoryDto() throws Exception {
        Long categoryId = 1L;
        String updatedName = "UpdatedName";
        String updatedDescription = "UpdatedDescription";

        CreateCategoryRequestDto updateRequestDto =
                createCategoryRequestDtoDto(updatedName, updatedDescription);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(resultDto);
        assertEquals(categoryId, resultDto.id());
        assertEquals(updatedName, resultDto.name());
        assertEquals(updatedDescription, resultDto.description());
    }

    @Test
    @DisplayName("Update category by id with out authorities")
    @WithMockUser(username = "user", authorities = {"USER"})
    void update_UpdateCategoryWithoutPermission_ShouldReturnForbidden() throws Exception {
        Long categoryId = 1L;
        String updatedName = "UpdatedName";
        String updatedDescription = "UpdatedDescription";

        CreateCategoryRequestDto updateRequestDto =
                createCategoryRequestDtoDto(updatedName, updatedDescription);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("Delete category by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteById_DeleteCategory_ShouldReturnNoContent() throws Exception {
        Long categoryIdToDelete = 1L;

        MvcResult result = mockMvc.perform(delete("/categories/{id}", categoryIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Delete category by id with out authorities")
    @WithMockUser(username = "user", authorities = {"USER"})
    void deleteById_DeleteCategoryWithoutPermission_ShouldReturnForbidden() throws Exception {
        Long categoryIdToDelete = 1L;

        MvcResult result = mockMvc.perform(delete("/categories/{id}", categoryIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private CategoryDto createCategoryDto(String name, String description) {
        CategoryDto categoryDto = new CategoryDto(1L, name, description);
        return categoryDto;
    }

    private CreateCategoryRequestDto createCategoryRequestDtoDto(String name, String description) {
        return new CreateCategoryRequestDto(name, description);
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
