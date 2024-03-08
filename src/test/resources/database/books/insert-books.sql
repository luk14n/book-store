INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Test Title 1', 'Test Author 1', '1234567890', 100, 'Test description 1', 'Test cover image 1', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'Test Title 2', 'Test Author 2', '3456457896', 100, 'Test description 2', 'Test cover image 2', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (3, 'Test Title 3', 'Test Author 3', '1234545367', 100, 'Test description 3', 'Test cover image 3', FALSE);
INSERT INTO books_categories (book_id, category_id) VALUES (3, 2);
