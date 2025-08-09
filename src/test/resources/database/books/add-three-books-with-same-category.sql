INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
(1, 'Catching Fire', 'Suzanne Collins', '9826307743657', 19.99, 'The second book of “The Hunger Games” trilogy', NULL, false),
(2, 'Harry Potter and the Deathly Hallows', 'J.K. Rowling', '9826063743657', 17.99, 'The story of Harry Potters last battle against Voldemort', NULL, false),
(3, 'Glass Girls','Danie Shokoohi', '98260326543657', 14.99,'Long estranged from her family, Alice is haunted by memories of her own abusive mother', NULL, false);

INSERT INTO books_categories (book_id, category_id) VALUES
(1, 1),
(2, 1),
(3, 2);
