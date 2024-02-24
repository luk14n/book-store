package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.model.Book;
import com.lukian.bookstore.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> categoriesFromModel = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoriesIds(categoriesFromModel);
    }
}

