package com.lukian.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    public static final String ISBN_REGEXP
            = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$";
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Pattern(regexp = ISBN_REGEXP,
            message = "Invalid ISBN format")
    private String isbn;
    @NotNull
    @Positive
    @Min(1)
    private BigDecimal price;
    @NotBlank
    private String description;
    private String coverImage;
}
