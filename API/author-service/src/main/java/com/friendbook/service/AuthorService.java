package com.friendbook.service;

import java.util.List;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorForSearch;
import com.friendbook.DTO.AuthorWithBooks;
import com.friendbook.model.Author;

public interface AuthorService {
    Author getAuthor(Long id);
    AuthorWithBooks getAuthorWithBooks(Long authorId);

    AuthorForBook getAuthorForBook(Long authorId);

    List<AuthorForSearch> getAuthorsByAuthorName(String name);
}
