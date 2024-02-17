package com.lukian.bookstore.controller;

import com.lukian.bookstore.dto.BookDto;
import com.lukian.bookstore.dto.BookSearchParametersDto;
import com.lukian.bookstore.dto.CreateBookRequestDto;
import com.lukian.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book management", description = "Endpoints for managing books in the online store")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books",
            description = "Get a list of all available books with optional pagination")
    public List<BookDto> getAll(@ParameterObject Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID",
            description = "Get a specific book based on its unique ID")
    public BookDto getBookById(@PathVariable(name = "id") Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Add a new book to the online store")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the book",
            description = "Update an existing book by specifying new desired parameters")
    public BookDto updateBook(@PathVariable(name = "id") Long id,
                              @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the book",
            description = "Delete a specific book from the online store by its unique ID")
    public void deleteBook(@PathVariable(name = "id") Long id) {
        bookService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for the book",
            description = "Dynamically specify criteria to filter desired books")
    public List<BookDto> searchBooks(@ParameterObject BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }
}
