package com.lukian.bookstore.mapper;

import com.lukian.bookstore.dto.BookDto;
import com.lukian.bookstore.dto.CreateBookRequestDto;
import com.lukian.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
