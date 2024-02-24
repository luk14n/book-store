package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.category.CategoryDto;
import com.lukian.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryDto categoryDtoO);

    /**
     * Updates an existing Category entity based on the information provided in the CategoryDto.
     *
     * This method is responsible for updating the attributes of an existing Category
     * entity using the data present in the provided CategoryDto. The @MappingTarget annotation
     * indicates that the updates should be applied directly to the existing Category instance.
     * All realisation is performed automatically in target/generated-sources
     *
     * @param categoryDto The CategoryDto containing the updated information.
     * @param category The Category entity to be updated.
     */

    void updateFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
