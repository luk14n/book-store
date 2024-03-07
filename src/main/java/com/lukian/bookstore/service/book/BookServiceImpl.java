package com.lukian.bookstore.service.book;

import com.lukian.bookstore.dto.book.BookDto;
import com.lukian.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.lukian.bookstore.dto.book.BookSearchParametersRequestDto;
import com.lukian.bookstore.dto.book.CreateBookRequestDto;
import com.lukian.bookstore.mapper.BookMapper;
import com.lukian.bookstore.model.Book;
import com.lukian.bookstore.repository.book.BookRepository;
import com.lukian.bookstore.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find book by if: " + id));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book bookFromDb = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find book by id: " + id));

        bookMapper.updateFromDto(requestDto, bookFromDb);

        return bookMapper.toDto(bookRepository.save(bookFromDb));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersRequestDto searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
