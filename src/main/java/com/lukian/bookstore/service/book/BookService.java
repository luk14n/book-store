package com.lukian.bookstore.service.book;

import com.lukian.bookstore.dto.book.BookCreateRequestDto;
import com.lukian.bookstore.dto.book.BookResponseDto;
import com.lukian.bookstore.dto.book.BookSearchParametersRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(BookCreateRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    BookResponseDto update(Long id, BookCreateRequestDto requestDto);

    void delete(Long id);

    List<BookResponseDto> search(BookSearchParametersRequestDto searchParameters);
}
