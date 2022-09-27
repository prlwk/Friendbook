package com.friendbook.bookservice.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT new com.friendbook.bookservice.DTO.BookForAuthor(b.id, b.rating, b.linkCover)" +
            " FROM Book b inner join b.authors a where a.id = :authorId ORDER BY b.rating DESC")
    Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId);

    @Query("SELECT new com.friendbook.bookservice.DTO.BookForSearch(b) FROM Book b inner join b.authors a where a.id = :authorId ORDER BY b.rating DESC")
    Set<BookForSearch> getBooksByAuthorId(Long authorId);

    @Query("SELECT new com.friendbook.bookservice.DTO.BookForBookPage(b) FROM Book b where b.id = :bookId")
    Optional<BookForBookPage> getBookById(Long bookId);
}
