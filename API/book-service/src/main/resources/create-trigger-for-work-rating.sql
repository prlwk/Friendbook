create trigger updatingSumMarks1
    after update
    on books
    for each row
    update author.authors
    SET author.authors.rating =
            (select sum(book.books.sum_marks / book.books.count_marks) / count(book.books.id)
             from book.books
                      join book.book_author ON book.books.id = book.book_author.book_id
                      join book.authors ON book.authors.id = book_author.author_id
             WHERE book.authors.id = (select book.authors.id
                                      from book.books
                                               join book.book_author ON book.books.id = book.book_author.book_id
                                               join book.authors ON book.authors.id = book_author.author_id
                                      where book.books.id = NEW.id))
    WHERE author.authors.id
              = (select book.authors.id
                 from book.books
                          join book.book_author ON book.books.id = book.book_author.book_id
                          join book.authors ON book.authors.id = book_author.author_id
                 where book.books.id = NEW.id);

