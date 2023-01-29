package com.friendbook.bookservice;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import com.friendbook.bookservice.model.UserBooksWantToRead;
import com.friendbook.bookservice.repository.BookRepository;
import com.friendbook.bookservice.repository.UserBooksWantToReadRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserBooksWantToReadJPATests {
    @Autowired
    UserBooksWantToReadRepository userBooksWantToReadRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final Long idFirstBook = 1L;
    private final Long idSecondBook = 2L;
    private final Long idThirdBook = 3L;
    private UserBooksWantToRead userBooksWantToRead1;
    private UserBooksWantToRead userBooksWantToRead2;


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
        userBooksWantToRead1 = new UserBooksWantToRead(1L, bookRepository.findById(idFirstBook).get(), 1L, false);
        userBooksWantToRead2 = new UserBooksWantToRead(2L, bookRepository.findById(idThirdBook).get(), 1L, false);
        userBooksWantToReadRepository.save(userBooksWantToRead1);
        userBooksWantToReadRepository.save(userBooksWantToRead2);
    }

    @AfterEach
    public void clearData() {
        String queryForBooksTable = "DELETE FROM books";
        String queryForUserBooksTable = "DELETE FROM user_books_want_to_read";
        entityManager.getEntityManager().createNativeQuery(queryForBooksTable).executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForUserBooksTable).executeUpdate();
    }

    @Test
    public void getSavingBookByBookIdAndUserId() {
        boolean isSavingBookWithId1 = userBooksWantToReadRepository.getSavingBookByBookIdAndUserId(idFirstBook, 1L);
        assertTrue(isSavingBookWithId1);
        boolean isSavingBookWithId2 = userBooksWantToReadRepository.getSavingBookByBookIdAndUserId(idSecondBook, 1L);
        assertFalse(isSavingBookWithId2);
    }

    @Test
    public void getUserBooksWantToReadByBookAndUserIdAndDel() {
        Optional<UserBooksWantToRead> optionalUserBooksWantToRead = userBooksWantToReadRepository
                .getUserBooksWantToReadByBookAndUserIdAndDel(bookRepository.findById(idFirstBook).get(),
                        1L, false);
        assertTrue(optionalUserBooksWantToRead.isPresent());
        optionalUserBooksWantToRead = userBooksWantToReadRepository
                .getUserBooksWantToReadByBookAndUserIdAndDel(bookRepository.findById(idSecondBook).get(),
                        1L, false);
        assertFalse(optionalUserBooksWantToRead.isPresent());
    }

    @Test
    public void getSavingBooksIdByUserId() {
        List<Long> bookIdList = userBooksWantToReadRepository.getSavingBooksIdByUserId(1L);
        assertEquals(2, bookIdList.size());
        assertThat(bookIdList, hasItem(idFirstBook));
        assertThat(bookIdList, hasItem(idThirdBook));
        bookIdList = userBooksWantToReadRepository.getSavingBooksIdByUserId(2L);
        assertEquals(0, bookIdList.size());
    }
}
