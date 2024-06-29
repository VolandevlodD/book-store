-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate tables to remove all data and reset auto-increment values
TRUNCATE TABLE books_categories;
TRUNCATE TABLE categories;
TRUNCATE TABLE books;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
