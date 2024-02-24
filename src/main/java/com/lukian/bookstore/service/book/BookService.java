package com.lukian.bookstore.service.book;

import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.lukian.bookstore.dto.book.BookSearchParametersRequestDto;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersRequestDto searchParameters);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id);

}

