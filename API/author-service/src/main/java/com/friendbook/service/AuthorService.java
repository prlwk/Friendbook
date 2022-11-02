package com.friendbook.service;

import java.util.List;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorWithBooks;

public interface AuthorService {
    AuthorWithBooks getAuthor(Long authorId);

    AuthorForBook getAuthorForBook(Long authorId);

    List<AuthorForBook> getAuthorsByAuthorName(String name);
}
