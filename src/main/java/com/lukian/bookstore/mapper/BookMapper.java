package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);
}

