package com.lukian.bookstore.service.category;

import com.lukian.bookstore.dto.category.CategoryDto;
import com.lukian.bookstore.mapper.CategoryMapper;
import com.lukian.bookstore.model.Category;
import com.lukian.bookstore.repository.category.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot get Category by id: " + id)));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(categoryDto)));
    }

    /**
     * Updates an existing Category in the database based on the provided ID & DTO.
     *
     * This method retrieves the Category from the database using the given ID,
     * then updates its attributes with the values provided in the CategoryDto using mapper.
     * The updated Category is then saved back to the database.
     *
     * @param id The ID of the Category to be updated.
     * @param categoryDto The CategoryDto containing the updated information.
     * @return The updated CategoryDto after the changes are persisted in the database.
     * @throws RuntimeException if no Category with the given ID is found in the database.
     */
    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category categoryFromDb = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot get Category by id: " + id));

        categoryMapper.updateFromDto(categoryDto, categoryFromDb);

        return categoryMapper.toDto(categoryRepository.save(categoryFromDb));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
