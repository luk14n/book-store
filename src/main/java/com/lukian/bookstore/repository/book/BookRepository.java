package com.lukian.bookstore.repository.book;

import com.lukian.bookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    /**
     * {@code LEFT JOIN FETCH } used in order to prevent LIE
     * when initializing Book class with categories field.
     * Nested query in order to access categories ids
     */
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories c "
            + "WHERE :categoryId IN (SELECT cat.id FROM b.categories cat)")
    List<Book> findAllByCategoryId(Long categoryId);
}
