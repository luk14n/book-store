package com.lukian.bookstore.service;

import com.lukian.bookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
