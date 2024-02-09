package com.lukian.bookstore.service;

import java.util.List;
import com.lukian.bookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
