-- Вимкнути перевірку зовнішніх ключів
SET FOREIGN_KEY_CHECKS = 0;

-- Очистити таблиці
TRUNCATE TABLE books_categories;
TRUNCATE TABLE books;
TRUNCATE TABLE categories;

-- Увімкнути перевірку зовнішніх ключів
SET FOREIGN_KEY_CHECKS = 1;
