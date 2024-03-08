DELETE FROM books_categories
WHERE book_id IN
      (SELECT id FROM books WHERE title = 'Test Title 3'
                              AND author = 'Test Author 3'
                              AND isbn = '1234545367'
                              AND price = 100);

DELETE FROM books
WHERE title = 'Test Title 3'
  AND author = 'Test Author 3'
  AND isbn = '1234545367'
  AND price = 100;
