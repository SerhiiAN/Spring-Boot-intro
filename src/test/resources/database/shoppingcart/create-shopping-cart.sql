INSERT INTO roles (id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'USER');
INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES
    (1, 'jane.doe@example.com', 'password1', 'John', 'Doe', '123 Main St', 0),
    (2, 'user2@example.com', 'password2', 'Jane', 'Smith', '456 Elm St', 0);

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE email = 'jane.doe@example.com'), (SELECT id FROM roles WHERE name = 'USER')),
    ((SELECT id FROM users WHERE email = 'user2@example.com'), (SELECT id FROM roles WHERE name = 'ADMIN'));

INSERT INTO books (title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    ('Book Title 1', 'Author 1', '9781234567897', 19.99, 'Description for Book 1', 'https://example.com/cover1.jpg', 0),
    ('Book Title 2', 'Author 2', '9781234567892', 29.99, 'Description for Book 2', 'https://example.com/cover2.jpg', 0),
    ('Book Title 3', 'Author 3', '9781234567893', 39.99, 'Description for Book 3', 'https://example.com/cover3.jpg', 0);

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES
    (1, 0),
    (2, 0);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES
    (1, 1, 1, 5);
