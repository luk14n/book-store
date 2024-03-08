package com.lukian.bookstore.service.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.mapper.BookMapper;
import com.lukian.bookstore.model.Book;
import com.lukian.bookstore.model.Category;
import com.lukian.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify whether returned BookDto is valid when calling save() method")
    public void saveBook_WithValidRequestDto_ShouldReturnValidBookDto() {
        // Given
        Category testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");

        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Test Book",
                "Test author",
                "1234567891234",
                BigDecimal.valueOf(20.99),
                "Test Description",
                "Test Cover Image",
                Set.of(testCategory));

        Book book = new Book();
        book.setId(1L);
        book.setTitle(requestDto.title());
        book.setAuthor(requestDto.author());
        book.setDescription(requestDto.description());
        book.setIsbn(requestDto.isbn());
        book.setPrice(requestDto.price());
        book.setCoverImage(requestDto.coverImage());
        book.setCategories(requestDto.categories());
        book.setDeleted(false);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setDescription(book.getDescription());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setCoverImage(book.getCoverImage());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto savedBookDto = bookService.save(requestDto);

        // Then
        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);

    }

    @Test
    @DisplayName("Verify whether IllegalArgumentException "
            + "is thrown when calling findById() with invalid ID")
    void getBook_WithInvalidId_ShouldThrowIllegalArgumentException() {
        // Given
        Long invalidId = 999L;

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> bookService.findById(invalidId));
    }

    @Test
    @DisplayName("Verify whether IllegalArgumentException "
            + "is thrown when calling update() with invalid ID")
    void updateBook_WithInvalidId_ShouldThrowIllegalArgumentException() {
        // Given
        Long invalidId = 999L;
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto(
                "Updated Title",
                "Updated Author",
                "9876543210987",
                BigDecimal.valueOf(25.99),
                "Updated Description",
                "Updated Cover Image",
                Set.of(new Category()));

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> bookService.update(invalidId, updateRequestDto));
    }

    @Test
    @DisplayName("Verify whether delete() method deletes the book successfully")
    void deleteBook_WithValidId_ShouldDeleteBook() {
        // Given
        Long testId = 1L;

        // When
        bookService.delete(testId);

        // Then
        verify(bookRepository, times(1)).deleteById(testId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify whether findAll() returns "
            + "a list of BookDto when calling findAll() method")
    void findAll_ShouldReturnListOfBookDto() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> booksFromRepository = Arrays.asList(
                Book.builder()
                        .id(1L)
                        .title("Title1")
                        .author("Author1")
                        .isbn("1234567890123")
                        .price(BigDecimal.valueOf(20.99))
                        .description("Description1")
                        .coverImage("CoverImage1")
                        .categories(Set.of(new Category()))
                        .build(),
                Book.builder()
                        .id(2L)
                        .title("Title2")
                        .author("Author2")
                        .isbn("2345678901234")
                        .price(BigDecimal.valueOf(25.99))
                        .description("Description2")
                        .coverImage("CoverImage2")
                        .categories(Set.of(new Category()))
                        .build()
        );

        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(booksFromRepository));
        when(bookMapper.toDto(any())).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return BookDto.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .description(book.getDescription())
                    .coverImage(book.getCoverImage())
                    .categoriesIds(book.getCategories().stream().map(Category::getId).toList())
                    .build();
        });

        // When
        List<BookDto> resultDtos = bookService.findAll(pageable);

        // Then
        assertThat(resultDtos).hasSize(booksFromRepository.size());

        assertThat(resultDtos.get(0).getTitle()).isEqualTo("Title1");
        assertThat(resultDtos.get(1).getAuthor()).isEqualTo("Author2");

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(booksFromRepository.size())).toDto(any());
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}

