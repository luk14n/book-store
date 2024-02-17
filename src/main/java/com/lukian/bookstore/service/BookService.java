package com.lukian.bookstore.service;

import com.lukian.bookstore.dto.BookDto;
import com.lukian.bookstore.dto.BookSearchParametersDto;
import com.lukian.bookstore.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
