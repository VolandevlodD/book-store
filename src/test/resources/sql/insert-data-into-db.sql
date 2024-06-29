-- Insert books with cover image
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES
    (1, 'War and Peace', 'Leo Tolstoy', '978-0-1404-4850-4', 19.99, 'A detailed description of the history of the Napoleonic wars.', 'cover-image-url-1'),
    (2, 'Pride and Prejudice', 'Jane Austen', '978-1-85326-000-2', 9.99, 'A classic novel of manners and marriage in early 19th century England.', 'cover-image-url-2'),
    (3, '1984', 'George Orwell', '978-0-452-28423-4', 14.99, 'A dystopian novel set in a totalitarian society under constant surveillance.', 'cover-image-url-3'),
    (4, 'The Great Gatsby', 'F. Scott Fitzgerald', '978-0-7432-7356-5', 10.99, 'A tragic story of Jay Gatsby and his unrequited love for Daisy Buchanan.', 'cover-image-url-4'),
    (5, 'To Kill a Mockingbird', 'Harper Lee', '978-0-06-112008-4', 18.99, 'A novel about the serious issues of rape and racial inequality in the American South.', 'cover-image-url-5');

-- Insert categories
INSERT INTO categories (id, name, description)
VALUES
    (1, 'Historical Fiction', 'A genre of fiction that takes place in the past.'),
    (2, 'Romance', 'A genre focused on romantic love and relationships.'),
    (3, 'Dystopian', 'A genre that explores social and political structures in a dark, nightmare world.'),
    (4, 'Classic', 'Timeless works of literature recognized for their enduring quality and influence.'),
    (5, 'Drama', 'A genre that relies on emotional and relational development of realistic characters.');

-- Insert books_categories associations
INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5);
