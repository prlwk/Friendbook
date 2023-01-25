package com.friendbook.bookservice;

import java.util.ArrayList;
import java.util.List;
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
import com.friendbook.bookservice.DTO.GenreForBook;
import com.friendbook.bookservice.repository.GenreRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenreJPATests {
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final Long idFirstGenre = 1L;
    private final Long idSecondGenre = 2L;
    private final Long idThirdGenre = 3L;

    @BeforeEach
    void initData() {
        genreRepository.deleteAll();
        String queryForGenresTable = "INSERT INTO genres (id, name) VALUES (:1, :2)";
        entityManager.getEntityManager().createNativeQuery(queryForGenresTable)
                .setParameter("1", idFirstGenre)
                .setParameter("2", "novel")
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForGenresTable)
                .setParameter("1", idSecondGenre)
                .setParameter("2", "comedy")
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForGenresTable)
                .setParameter("1", idThirdGenre)
                .setParameter("2", "horror")
                .executeUpdate();
    }

    @AfterEach
    public void clearData() {
        genreRepository.deleteAll();
    }

    @Test
    public void getAllGenres() {
        List<GenreForBook> genreForBookList = genreRepository.getAll();
        assertEquals(3, genreForBookList.size());
        List<Long> idGenreForBookList = new ArrayList<>();
        for (GenreForBook genreForBook : genreForBookList) {
            idGenreForBookList.add(genreForBook.getId());
        }
        assertThat(idGenreForBookList, hasItem(idFirstGenre));
        assertThat(idGenreForBookList, hasItem(idSecondGenre));
        assertThat(idGenreForBookList, hasItem(idThirdGenre));
        genreRepository.deleteById(idFirstGenre);
        genreForBookList = genreRepository.getAll();
        assertEquals(2, genreForBookList.size());
        idGenreForBookList = new ArrayList<>();
        for (GenreForBook genreForBook : genreForBookList) {
            idGenreForBookList.add(genreForBook.getId());
        }
        assertThat(idGenreForBookList, hasItem(idThirdGenre));
        assertThat(idGenreForBookList, hasItem(idSecondGenre));
    }
}
