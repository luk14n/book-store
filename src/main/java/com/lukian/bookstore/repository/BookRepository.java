package com.lukian.bookstore.repository;

import java.util.List;
import com.lukian.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
