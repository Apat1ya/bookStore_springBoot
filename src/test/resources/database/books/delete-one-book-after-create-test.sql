DELETE bc
FROM books_categories bc
JOIN books b ON bc.book_id = b.id
WHERE b.title = '1408';

DELETE FROM books
WHERE title = '1408';