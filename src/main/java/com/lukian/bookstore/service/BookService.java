package com.lukian.bookstore.service;

import com.lukian.bookstore.dto.BookDto;
import com.lukian.bookstore.dto.BookSearchParametersDto;
import com.lukian.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
