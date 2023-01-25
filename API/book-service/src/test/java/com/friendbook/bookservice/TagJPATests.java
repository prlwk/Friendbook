package com.friendbook.bookservice;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import com.friendbook.bookservice.DTO.TagForBook;
import com.friendbook.bookservice.repository.TagRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TagJPATests {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final Long idFirstTag = 1L;
    private final Long idSecondTag = 2L;
    private final Long idThirdTag = 3L;

    @BeforeEach
    void initData() {
        tagRepository.deleteAll();
        String queryForTagTable = "INSERT INTO tags (id, name) VALUES (:1, :2)";
        entityManager.getEntityManager().createNativeQuery(queryForTagTable)
                .setParameter("1", idFirstTag)
                .setParameter("2", "animal")
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForTagTable)
                .setParameter("1", idSecondTag)
                .setParameter("2", "space")
                .executeUpdate();
        entityManager.getEntityManager().createNativeQuery(queryForTagTable)
                .setParameter("1", idThirdTag)
                .setParameter("2", "game")
                .executeUpdate();
    }

    @AfterEach
    public void clearData() {
        tagRepository.deleteAll();
    }

    @Test
    public void getAllTags() {
        List<TagForBook> tagForBookList = tagRepository.getAll();
        assertEquals(3, tagForBookList.size());
        List<Long> idTagForBookList = new ArrayList<>();
        for (TagForBook tagForBook : tagForBookList) {
            idTagForBookList.add(tagForBook.getId());
        }
        assertThat(idTagForBookList, hasItem(idFirstTag));
        assertThat(idTagForBookList, hasItem(idSecondTag));
        assertThat(idTagForBookList, hasItem(idThirdTag));
        tagRepository.deleteById(idFirstTag);
        tagForBookList = tagRepository.getAll();
        assertEquals(2, tagForBookList.size());
        idTagForBookList = new ArrayList<>();
        for (TagForBook tagForBook : tagForBookList) {
            idTagForBookList.add(tagForBook.getId());
        }
        assertThat(idTagForBookList, hasItem(idThirdTag));
        assertThat(idTagForBookList, hasItem(idSecondTag));
    }
}
