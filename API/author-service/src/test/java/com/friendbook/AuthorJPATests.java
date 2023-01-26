package com.friendbook;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.DTO.AuthorForSearch;
import com.friendbook.model.Author;
import com.friendbook.repository.AuthorRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorJPATests {
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    AuthorRepository authorRepository;

    private final Author author1 = new Author(1L, "Erich Maria Remarque", "1898-1970", "1.jpg", "Biography", 0L);
    private final Author author2 = new Author(2L, "William Shakespeare", "1564-1616", "2.jpg", "Biography", 5L);

    @BeforeEach
    void initData() {
        authorRepository.save(author1);
        authorRepository.save(author2);
    }

    @AfterEach
    public void clearData() {
        authorRepository.deleteAll();
    }

    @Test
    public void findAuthorWhenEnterPartOfName() {
        List<Author> authorList = authorRepository.getAuthorsByName("Erich", "", "");
        assertEquals(authorList.size(), 1);
        assertEquals(authorList.get(0).getName(), author1.getName());
    }

    @Test
    public void findAuthorWhenEnterIncompleteWordOfName() {
        List<Author> authorList = authorRepository.getAuthorsByName("Eri", "", "");
        assertEquals(authorList.size(), 1);
        assertEquals(authorList.get(0).getName(), author1.getName());
    }

    @Test
    public void findAuthorWhenEnterNameByLowerCase() {
        List<Author> authorList = authorRepository.getAuthorsByName("erich", "Maria", "remarque");
        assertEquals(authorList.size(), 1);
        assertEquals(authorList.get(0).getName(), author1.getName());
    }

    @Test
    public void findAuthorWhenEnterIncorrectPartOfName() {
        List<Author> authorList = authorRepository.getAuthorsByName("erich", "Maria", "Pushkin");
        assertEquals(authorList.size(), 0);
    }

    @Transactional
    @Modifying
    @Test
    public void updateCountRequestsById() {
        long currentCountRequests = author1.getCountRequests();
        authorRepository.updateCountRequestsByAuthorId(author1.getId());
        testEntityManager.clear();
        assertEquals(currentCountRequests + 1, authorRepository.findById(author1.getId()).get().getCountRequests());
    }

    @Test
    public void searchAuthorsSortingByCountRequests() {
        List<Long> listId = List.of(author1.getId(), author2.getId());
        List<AuthorForSearch> authorForSearchList
                = authorRepository.search(listId,
                        PageRequest.of(0, 2, Sort.by("countRequests").descending())).stream().toList();
        assertTrue(authorRepository.findById(authorForSearchList.get(0).getId()).get().getCountRequests()
                > authorRepository.findById(authorForSearchList.get(1).getId()).get().getCountRequests());
    }

    @Test
    public void searchAuthorsSortingByCertainOrder() {
        List<Long> listId = List.of(author2.getId(), author1.getId());
        List<AuthorForSearch> authorForSearchList
                = authorRepository.search(listId,
                PageRequest.of(0, 2,
                        JpaSort.unsafe("FIELD(a.id, :listId)"))).stream().toList();
        assertEquals(2L, authorForSearchList.get(0).getId());
        assertEquals(1L, authorForSearchList.get(1).getId());
        listId = List.of(author1.getId(), author2.getId());
        authorForSearchList = authorRepository.search(listId,
                PageRequest.of(0, 2,
                        JpaSort.unsafe("FIELD(a.id, :listId)"))).stream().toList();
        assertEquals(1L, authorForSearchList.get(0).getId());
        assertEquals(2L, authorForSearchList.get(1).getId());
    }
}
