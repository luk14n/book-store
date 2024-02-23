package com.lukian.bookstore.validation.isbn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomIsbnValidator implements
        ConstraintValidator<IsbnConstraint, String> {
    public static final String ISBN_REGEXP
            = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$";

    @Override
    public void initialize(IsbnConstraint isbn) {
    }

    @Override
    public boolean isValid(String isbn,
                           ConstraintValidatorContext cxt) {
        return isbn != null && isbn.matches(ISBN_REGEXP)
                && (isbn.length() >= 10) && (isbn.length() <= 13);
    }
}
