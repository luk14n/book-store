package com.lukian.bookstore.repository;

import com.lukian.bookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
