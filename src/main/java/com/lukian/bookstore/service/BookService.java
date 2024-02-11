package com.lukian.bookstore.service;

import com.lukian.bookstore.dto.BookDto;
import com.lukian.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
