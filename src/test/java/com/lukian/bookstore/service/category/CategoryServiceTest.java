package com.lukian.bookstore.service.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.lukian.bookstore.dto.category.CategoryDto;
import com.lukian.bookstore.dto.category.CreateCategoryRequestDto;
import com.lukian.bookstore.mapper.CategoryMapper;
import com.lukian.bookstore.model.Category;
import com.lukian.bookstore.repository.category.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify whether returned CategoryDto is valid when calling getById() method")
    void getCategory_WithValidId_ShouldReturnValidCategoryDto() {
        // Given
        Long testId = 10L;
        Category category = new Category();
        category.setId(testId);
        category.setDescription("Test Description");
        category.setName("Test name");

        CategoryDto categoryDto = new CategoryDto(10L, "Test name", "Test description");

        when(categoryRepository.findById(testId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        String actual = categoryService.getById(testId).name();

        // Then
        String expected = "Test name";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify whether returned CategoryDto is Valid when calling save() method")
    void saveCategory_WithValidRequestDto_ShouldReturnCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Test name", "Test description");

        Category category = new Category();
        category.setId(1L);
        category.setDescription(requestDto.description());
        category.setName(requestDto.name());

        CategoryDto categoryDto = new CategoryDto(
                category.getId(), category.getName(), category.getDescription());

        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryMapper.toModel(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        // When
        CategoryDto savedCategoryDto = categoryService.save(categoryDto);

        // Then
        assertThat(savedCategoryDto).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify whether IllegalArgumentException "
            + "is thrown when calling getById() with invalid ID")
    void getCategory_WithInvalidId_ShouldThrowIllegalArgumentException() {
        // Given
        Long invalidId = 999L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> categoryService.getById(invalidId));
    }

    @Test
    @DisplayName("Verify whether CategoryDto is updated successfully when calling update() method")
    void updateCategory_WithValidIdAndDto_ShouldReturnUpdatedCategoryDto() {
        // Given
        Long testId = 1L;
        Category existingCategory = new Category();
        existingCategory.setId(testId);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        CategoryDto updatedCategoryDto = new CategoryDto(
                testId, "New Name", "New Description");
        Category updatedCategory = new Category();
        updatedCategory.setId(testId);
        updatedCategory.setName(updatedCategoryDto.name());
        updatedCategory.setDescription(updatedCategoryDto.description());

        when(categoryRepository.findById(testId)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        // When
        CategoryDto resultDto = categoryService.update(testId, updatedCategoryDto);

        // Then
        assertThat(resultDto).isEqualTo(updatedCategoryDto);
        verify(categoryRepository, times(1)).findById(testId);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    @DisplayName("Verify whether IllegalArgumentException "
            + "is thrown when calling update() with invalid ID")
    void updateCategory_WithInvalidId_ShouldThrowIllegalArgumentException() {
        // Given
        Long invalidId = 999L;
        CategoryDto updatedCategoryDto = new CategoryDto(
                invalidId, "New Name", "New Description");

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.update(invalidId, updatedCategoryDto));
    }

    @Test
    @DisplayName("Verify whether deleteById() method deletes the category successfully")
    void deleteCategory_WithValidId_ShouldDeleteCategory() {
        // Given
        Long testId = 1L;

        // When
        categoryService.deleteById(testId);

        // Then
        verify(categoryRepository, times(1)).deleteById(testId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

}

