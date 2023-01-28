package com.friendbook.bookservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.model.UserBooksGrade;
import com.friendbook.bookservice.repository.BookRepository;
import com.friendbook.bookservice.repository.UserBooksGradeRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserBooksGradeJPATests {
    @Autowired
    UserBooksGradeRepository userBooksGradeRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final Long idFirstBook = 1L;
    private final Long idSecondBook = 2L;
    private final Long idThirdBook = 3L;
    private UserBooksGrade userBooksGrade1;
    private UserBooksGrade userBooksGrade2;

    @BeforeEach
    void initData() {
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
        userBooksGrade1 = new UserBooksGrade(1L, bookRepository.findById(1L).get(), 1L, 10, false);
        userBooksGrade2 = new UserBooksGrade(2L, bookRepository.findById(3L).get(), 1L, 7, false);
        userBooksGradeRepository.save(userBooksGrade1);
        userBooksGradeRepository.save(userBooksGrade2);
    }

    @AfterEach
    public void clearData() {
        String queryForBooksTable = "DELETE FROM books";
        String queryForUserBooksTable = "DELETE FROM user_books_grade";
        entityManager.getEntityManager().createNativeQuery(queryForBooksTable).executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForUserBooksTable).executeUpdate();
    }

    @Test
    public void getRatedBooksIdByUserId() {
        List<Long> listBookId = userBooksGradeRepository.getRatedBooksIdByUserId(1L);
        assertEquals(2, listBookId.size());
        assertThat(listBookId, hasItem(1L));
        assertThat(listBookId, hasItem(3L));
    }

    @Test
    public void getGradeByBookIdAndUserId() {
        Optional<Integer> grade = userBooksGradeRepository.getGradeByBookIdAndUserId(1L, 1L);
        assertEquals(10, grade.get());
        grade = userBooksGradeRepository.getGradeByBookIdAndUserId(2L, 1L);
        assertTrue(grade.isEmpty());
    }

    @Test
    public void getUserBooksGradeByBookIdAndUserId() {
        Optional<UserBooksGrade> userBooksGrade
                = userBooksGradeRepository.getUserBooksGradeByBookIdAndUserId(3L, 1L);
        assertEquals(1, userBooksGrade.get().getUserId());
        assertEquals(7, userBooksGrade.get().getGrade());
        assertEquals(3L, userBooksGrade.get().getBook().getId());
        userBooksGrade = userBooksGradeRepository.getUserBooksGradeByBookIdAndUserId(2L, 1L);
        assertTrue(userBooksGrade.isEmpty());
    }
}
