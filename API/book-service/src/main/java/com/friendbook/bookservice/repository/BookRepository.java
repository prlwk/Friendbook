package com.friendbook.bookservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new com.friendbook.bookservice.DTO.BookForAuthor(b.id, b.rating, b.linkCover) " +
            "FROM Book b inner join b.authors a where a.id = :authorId " +
            "ORDER BY b.rating DESC")
    Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId);

    @Query("SELECT new com.friendbook.bookservice.DTO.BookForSearch(b) " +
            "FROM Book b inner join b.authors a where a.id = :authorId " +
            "ORDER BY b.rating DESC")
    Set<BookForSearch> getBooksByAuthorId(Long authorId);

    @Query("SELECT new com.friendbook.bookservice.DTO.BookForBookPage(b) " +
            "FROM Book b where b.id = :bookId")
    Optional<BookForBookPage> getBookById(Long bookId);

    @Transactional
    @Modifying
    @Query("UPDATE Book b SET b.countRequests = b.countRequests + 1 WHERE b.id = :id")
    void updateCountRequestsById(Long id);

    @Query(value = "SELECT new com.friendbook.bookservice.DTO.BookForSearch(b) " +
            "FROM Book b join b.genres g join b.tags t " +
            "WHERE b.rating BETWEEN :ratingStart and :ratingFinish " +
            "and (:numberOfGenres=0 or g.id in (:genreList)) " +
            "and (:numberOfTags=0 or t.id in (:tagList)) " +
            "and (:word IS NULL or LOWER(b.name) LIKE CONCAT('%',LOWER(:word),'%') or b.id in :idList) " +
            "GROUP BY b.id " +
            "HAVING count(b.id) = :numberOfTags * :numberOfGenres or :numberOfTags + :numberOfGenres = 0",
            countQuery ="SELECT count(b) " +
                    "FROM Book b join b.genres g join b.tags t " +
                    "WHERE b.rating BETWEEN :ratingStart and :ratingFinish " +
                    "and (:numberOfGenres=0 or g.id in (:genreList)) " +
                    "and (:numberOfTags=0 or t.id in (:tagList)) " +
                    "and (:word IS NULL or LOWER(b.name) LIKE CONCAT('%',LOWER(:word),'%') or b.id in :idList)" +
                    "GROUP BY b.id " +
                    "HAVING count(b.id) = :numberOfTags * :numberOfGenres or :numberOfTags + :numberOfGenres = 0")
    Page<BookForSearch> getBooksBySearch(double ratingStart,
                                         double ratingFinish,
                                         String word,
                                         List<Long> idList,
                                         List<Long> genreList,
                                         Integer numberOfGenres,
                                         List<Long> tagList,
                                         Integer numberOfTags,
                                         Pageable pageable);
}
