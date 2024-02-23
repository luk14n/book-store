package com.lukian.bookstore.repository;

import com.lukian.bookstore.dto.book.BookSearchParametersRequestDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersRequestDto searchParametersDto);
}
