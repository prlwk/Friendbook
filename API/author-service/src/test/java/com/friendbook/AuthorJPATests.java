package com.friendbook;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import com.friendbook.model.Author;
import com.friendbook.repository.AuthorRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorJPATests {
    @Autowired
    AuthorRepository authorRepository;

    private final Author author = new Author(1L, "Erich Maria Remarque", "1898-1970", "1.jpg", "Biography");

    @BeforeEach
    void initData() {
        authorRepository.save(author);
    }

    @AfterEach
    public void clearData() {
        authorRepository.deleteAll();
    }

    @Test
    public void findAuthorWhenEnterPartOfName() {
        List<Author> authorList = authorRepository.getAuthorByName("Erich", "", "");
        assertEquals(authorList.size(), 1);
        assertEquals(authorList.get(0).getName(), author.getName());
    }

    @Test
    public void findAuthorWhenEnterIncompleteWordOfName() {
        List<Author> authorList = authorRepository.getAuthorByName("Eri", "", "");
        assertEquals(authorList.size(), 1);
        assertEquals(authorList.get(0).getName(), author.getName());
    }

    @Test
    public void findAuthorWhenEnterNameByLowerCase() {
        List<Author> authorList = authorRepository.getAuthorByName("erich", "Maria", "remarque");
        assertEquals(authorList.size(), 1);
        assertEquals(authorList.get(0).getName(), author.getName());
    }

    @Test
    public void findAuthorWhenEnterIncorrectPartOfName() {
        List<Author> authorList = authorRepository.getAuthorByName("erich", "Maria", "Pushkin");
        assertEquals(authorList.size(), 0);
    }
}
