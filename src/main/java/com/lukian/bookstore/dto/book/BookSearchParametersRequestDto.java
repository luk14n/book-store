package com.lukian.bookstore.dto.book;

public record BookSearchParametersRequestDto(
        String[] titles,
        String[] authors
) {}
