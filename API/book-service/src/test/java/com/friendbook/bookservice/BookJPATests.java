package com.friendbook.bookservice;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Query;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.repository.BookRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookJPATests {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final Long idFirstBook = 1L;
    private final Long idSecondBook = 2L;
    private final Long idThirdBook = 3L;

    @BeforeEach
    void initData() {
        bookRepository.deleteAll();
        String queryForBooksTable = "INSERT INTO books (id, name, count_marks, sum_marks, count_requests) VALUES (:1, :2, :3, :4, :5)";
        entityManager.getEntityManager().createNativeQuery(queryForBooksTable)
                .setParameter("1", idFirstBook)
                .setParameter("2", "Crime and Punishment")
                .setParameter("3", 3)
                .setParameter("4", 30)
                .setParameter("5", 0)
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForBooksTable)
                .setParameter("1", idSecondBook)
                .setParameter("2", "War and Peace")
                .setParameter("3", 2)
                .setParameter("4", 11)
                .setParameter("5", 0)
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForBooksTable)
                .setParameter("1", idThirdBook)
                .setParameter("2", "The Cherry Orchard")
                .setParameter("3", 1)
                .setParameter("4", 3)
                .setParameter("5", 0)
                .executeUpdate();
        String queryForAuthorsTable = "INSERT INTO authors (id) VALUES (:1)";
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsTable)
                .setParameter("1", 1)
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsTable)
                .setParameter("1", 2)
                .executeUpdate();
        String queryForAuthorsAnbBooksTable = "INSERT INTO book_author (book_id, author_id) VALUES (:1, :2)";
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsAnbBooksTable)
                .setParameter("1", 1)
                .setParameter("2", 1)
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsAnbBooksTable)
                .setParameter("1", 2)
                .setParameter("2", 1)
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsAnbBooksTable)
                .setParameter("1", 3)
                .setParameter("2", 2)
                .executeUpdate();
    }

    /*  @AfterEach
    public void clearData() {
        String queryForAuthorsAnbBooksTable = "DELETE FROM book_author";
        String queryForAuthorsTable = "DELETE FROM authors";
        String queryForBooksTable = "DELETE FROM books";
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsAnbBooksTable).executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForAuthorsTable).executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForBooksTable).executeUpdate();
    }*/

    @Test
    public void getBooksByAuthorIdForAuthorPage() {
        Set<BookForAuthor> bookForAuthorSet = bookRepository.getBooksByAuthorIdForAuthorPage(1L);
        assertEquals(bookForAuthorSet.size(), 2);
        List<Long> idList = new ArrayList<>();
        for (BookForAuthor book : bookForAuthorSet) {
            idList.add(book.getId());
        }
        assertThat(idList, hasItem(idFirstBook));
        assertThat(idList, hasItem(idSecondBook));
    }

    @Test
    public void getBooksByAuthorIdForSearch() {
        Set<BookForSearch> bookForAuthorSet = bookRepository.getBooksByAuthorId(2L);
        assertEquals(bookForAuthorSet.size(), 1);
        List<Long> idList = new ArrayList<>();
        for (BookForSearch book : bookForAuthorSet) {
            idList.add(book.getId());
        }
        assertThat(idList, hasItem(idThirdBook));
    }

    @Test
    public void getBookById() {
        Optional<BookForBookPage> book = bookRepository.getBookById(idFirstBook);
        assertTrue(book.isPresent());
        assertEquals(book.get().getId(), idFirstBook);
        book = bookRepository.getBookById(4L);
        assertFalse(book.isPresent());
    }

    @Test
    public void updateCountRequestsById() {
        String queryString = "SELECT count_requests FROM books where id = :1";
        Query query = entityManager.getEntityManager().createNativeQuery(queryString);
        query.setParameter("1", idFirstBook);
        long countRequests = ((BigInteger) query.getSingleResult()).longValue();
        bookRepository.updateCountRequestsById(idFirstBook);
        query = entityManager.getEntityManager().createNativeQuery(queryString);
        query.setParameter("1", idFirstBook);
        assertEquals(countRequests + 1, ((BigInteger) query.getSingleResult()).longValue());
    }

    @Test
    public void getBooksByBooksId() {
        List<Long> idList = List.of(idFirstBook, idSecondBook, idThirdBook);
        Set<BookForSearch> list = bookRepository.getBooksByBooksId(idList);
        assertEquals(idList.size(), list.size());
        idList = List.of(idFirstBook, idThirdBook);
        list = bookRepository.getBooksByBooksId(idList);
        assertEquals(idList.size(), list.size());
        List<Long> idListByResult = new ArrayList<>();
        for (BookForSearch book : list) {
            idListByResult.add(book.getId());
        }
        assertThat(idListByResult, hasItem(idFirstBook));
        assertThat(idListByResult, hasItem(idThirdBook));
    }

    @Test
    public void getBooksBySearchCheckingRating() {
        Page<BookForSearch> bookForSearchPage = bookRepository.getBooksBySearch(0, 10,
                null, null, null, 0, null, 0,
                PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(b.sumMarks + 0.0) / (b.countMarks + 0.0)")));
        assertEquals(3, bookForSearchPage.toList().size());
        List<BookForSearch> books = bookForSearchPage.toList();
        for (BookForSearch book : books) {
            assertTrue(book.getRating() >= 0);
            assertTrue(book.getRating() <= 10);
        }
        bookForSearchPage = bookRepository.getBooksBySearch(0, 9,
                null, null, null, 0, null, 0,
                PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(b.sumMarks + 0.0) / (b.countMarks + 0.0)")));
        assertEquals(2, bookForSearchPage.toList().size());
        books = bookForSearchPage.toList();
        for (BookForSearch book : books) {
            assertTrue(book.getRating() >= 0);
            assertTrue(book.getRating() <= 9);
        }
    }

    @Test
    public void getBooksBySearchByWord() {
        Page<BookForSearch> bookForSearchPage = bookRepository.getBooksBySearch(0, 10,
                "peace", null, null, 0, null, 0,
                PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(b.sumMarks + 0.0) / (b.countMarks + 0.0)")));
        assertEquals(1, bookForSearchPage.toList().size());
        assertTrue(bookForSearchPage.toList().get(0).getName().toLowerCase().contains("peace"));
    }

    @Test
    public void getBooksBySearchByGenres() {
        String queryForGenresTableForDeleting = "DELETE FROM genres";
        entityManager.getEntityManager().createNativeQuery(queryForGenresTableForDeleting).executeUpdate();
        String queryForGenresTableForInserting = "INSERT INTO genres (id, name) VALUES (:1, :2)";
        entityManager.getEntityManager().createNativeQuery(queryForGenresTableForInserting)
                .setParameter("1", 1)
                .setParameter("2", "novel")
                .executeUpdate();
        List<Long> listIdGenres = List.of(1L);
        String queryForGenresAndBookTable = "DELETE FROM book_genre";
        entityManager.getEntityManager().createNativeQuery(queryForGenresAndBookTable).executeUpdate();
        String queryForGenresAndBookTableForInserting = "INSERT INTO book_genre (book_id, genre_id) VALUES (:1, :2) ";
        entityManager.getEntityManager().createNativeQuery(queryForGenresAndBookTableForInserting)
                .setParameter("1", 1)
                .setParameter("2", 1)
                .executeUpdate();
        Page<BookForSearch> bookForSearchPage = bookRepository.getBooksBySearch(0, 10,
                null, null, listIdGenres, listIdGenres.size(), null, 0,
                PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(b.sumMarks + 0.0) / (b.countMarks + 0.0)")));
        assertEquals(1, bookForSearchPage.toList().size());
        entityManager.getEntityManager().createNativeQuery(queryForGenresTableForDeleting).executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForGenresAndBookTable).executeUpdate();
    }

    @Test
    public void getBooksBySearchByTags() {
        String queryForTagsTableForDeleting = "DELETE FROM tags";
        entityManager.getEntityManager().createNativeQuery(queryForTagsTableForDeleting).executeUpdate();
        String queryForTagsTableForInserting = "INSERT INTO tags (id, name) VALUES (:1, :2)";
        entityManager.getEntityManager().createNativeQuery(queryForTagsTableForInserting)
                .setParameter("1", 1)
                .setParameter("2", "animal")
                .executeUpdate();
        List<Long> listIdTags = List.of(1L);
        String queryForTagsAndBookTableForDeleting = "DELETE FROM book_tag";
        entityManager.getEntityManager().createNativeQuery(queryForTagsAndBookTableForDeleting).executeUpdate();
        String queryForTagsAndBookTableForInserting = "INSERT INTO book_tag (book_id, tag_id) VALUES (:1, :2) ";
        entityManager.getEntityManager().createNativeQuery(queryForTagsAndBookTableForInserting)
                .setParameter("1", 1)
                .setParameter("2", 1)
                .executeUpdate();
        Page<BookForSearch> bookForSearchPage = bookRepository.getBooksBySearch(0, 10,
                null, null, null, 0, listIdTags, listIdTags.size(),
                PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(b.sumMarks + 0.0) / (b.countMarks + 0.0)")));
        assertEquals(1, bookForSearchPage.toList().size());
        entityManager.getEntityManager().createNativeQuery(queryForTagsTableForDeleting).executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForTagsAndBookTableForDeleting).executeUpdate();
    }
}
