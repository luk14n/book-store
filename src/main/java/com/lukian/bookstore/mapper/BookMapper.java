package com.lukian.bookstore.mapper;

import com.lukian.bookstore.dto.BookDto;
import com.lukian.bookstore.dto.CreateBookRequestDto;
import com.lukian.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
