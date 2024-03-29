CREATE DATABASE BookStore;
USE BookStore;

CREATE TABLE Book (id INTEGER PRIMARY KEY, title TEXT, author TEXT, year INTEGER, isbn INTEGER);

INSERT INTO Book (id, title, author, year, isbn) VALUES (1, 'The Catcher in the Rye', 'J.D. Salinger', 1951, 1234567890);
INSERT INTO Book (id, title, author, year, isbn) VALUES (2, 'To Kill a Mockingbird', 'Harper Lee', 1960, 1234567891);
INSERT INTO Book (id, title, author, year, isbn) VALUES (3, '1984', 'George Orwell', 1949, 1234567892);
INSERT INTO Book (id, title, author, year, isbn) VALUES (4, 'Animal Farm', 'George Orwell', 1945, 1234567893);
INSERT INTO Book (id, title, author, year, isbn) VALUES (5, 'Brave New World', 'Aldous Huxley', 1932, 1234567894);
INSERT INTO Book (id, title, author, year, isbn) VALUES (6, 'The Great Gatsby', 'F. Scott Fitzgerald', 1925, 1234567895);
INSERT INTO Book (id, title, author, year, isbn) VALUES (7, 'The Grapes of Wrath', 'John Steinbeck', 1939, 1234567896);
INSERT INTO Book (id, title, author, year, isbn) VALUES (8, 'The Godfather', 'Mario Puzo', 1969, 1234567897);
INSERT INTO Book (id, title, author, year, isbn) VALUES (9, 'The Lord of the Rings', 'J.R.R. Tolkien', 1954, 1234567898);
INSERT INTO Book (id, title, author, year, isbn) VALUES (10, 'The Hobbit', 'J.R.R. Tolkien', 1937, 1234567899);
INSERT INTO Book (id, title, author, year, isbn) VALUES (11, 'The Da Vinci Code', 'Dan Brown', 2003, 1234567800);
INSERT INTO Book (id, title, author, year, isbn) VALUES (12, 'The Alchemist', 'Paulo Coelho', 1988, 1234567801);
INSERT INTO Book (id, title, author, year, isbn) VALUES (13, 'The Little Prince', 'Antoine de Saint-Exupéry', 1943, 1234567802);
INSERT INTO Book (id, title, author, year, isbn) VALUES (14, 'The Chronicles of Narnia', 'C.S. Lewis', 1950, 1234567803);
INSERT INTO Book (id, title, author, year, isbn) VALUES (15, 'The Hitchhiker''s Guide to the Galaxy', 'Douglas Adams', 1979, 1234567804);
