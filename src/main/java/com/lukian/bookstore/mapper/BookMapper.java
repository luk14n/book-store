package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.model.Book;
import com.lukian.bookstore.model.Category;
import java.util.List;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
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

    /**
     * Method creates book from ID during mapping process.
     * Need to add ID constructor to the Book class respectively
     *
     * @param id The identifier of the Book to be created.
     * @return A Book object initialized with the provided ID, or null if the ID is null.
     */

    @Named("bookFromId")
    default Book getBookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}

