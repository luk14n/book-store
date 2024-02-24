package com.lukian.bookstore.dto.book;

import com.lukian.bookstore.model.Category;
import com.lukian.bookstore.validation.isbn.IsbnConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public record CreateBookRequestDto(
        @NotBlank String title,
        @NotBlank String author,
        @IsbnConstraint String isbn,
        @NotNull @Positive @Min(1) BigDecimal price,
        @NotBlank String description,
        String coverImage,
        Set<Category> categories
) {}
