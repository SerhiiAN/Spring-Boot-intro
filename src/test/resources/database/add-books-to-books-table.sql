INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Book Title 1', 'Author 1', '9781234567891', 19.99, 'Description 1', 'https://example.com/cover1.jpg'),
('Book Title 2', 'Author 2', '9781234567892', 29.99, 'Description 2', 'https://example.com/cover2.jpg'),
('Book Title 3', 'Author 3', '9781234567893', 39.99, 'Description 3', 'https://example.com/cover3.jpg'),
('Book Title 4', 'Author 4', '9781234567894', 49.99, 'Description 4', 'https://example.com/cover4.jpg'),
('Book Title 5', 'Author 5', '9781234567895', 59.99, 'Description 5', 'https://example.com/cover5.jpg'),
('Book Title 6', 'Author 6', '9781234567896', 69.99, 'Description 6', 'https://example.com/cover6.jpg');

INSERT INTO categories (name, description) VALUES
('Fiction', 'Fictional books'),
('Non-Fiction', 'Non-fictional books'),
('Science Fiction', 'Books about science fiction');

INSERT INTO books_categories (book_id, category_id) VALUES
((SELECT id FROM books WHERE title = 'Book Title 1'), (SELECT id FROM categories WHERE name = 'Fiction')),
((SELECT id FROM books WHERE title = 'Book Title 2'), (SELECT id FROM categories WHERE name = 'Non-Fiction')),
((SELECT id FROM books WHERE title = 'Book Title 3'), (SELECT id FROM categories WHERE name = 'Science Fiction')),
((SELECT id FROM books WHERE title = 'Book Title 4'), (SELECT id FROM categories WHERE name = 'Fiction')),
((SELECT id FROM books WHERE title = 'Book Title 5'), (SELECT id FROM categories WHERE name = 'Non-Fiction')),
((SELECT id FROM books WHERE title = 'Book Title 6'), (SELECT id FROM categories WHERE name = 'Science Fiction'));
