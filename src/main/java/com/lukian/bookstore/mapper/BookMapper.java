package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.book.BookCreateRequestDto;
import com.lukian.bookstore.dto.book.BookResponseDto;
import com.lukian.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public interface BookMapper {

    BookResponseDto toDto(Book book);

    Book toModel(BookCreateRequestDto requestDto);

    void updateFromDto(BookCreateRequestDto requestDto, @MappingTarget Book book);
}

