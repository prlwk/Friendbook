package com.friendbook.service;

import java.util.List;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorWithBooks;

public interface AuthorService {
    public AuthorWithBooks getAuthor(Long authorId);
    public AuthorForBook getAuthorForBook(Long authorId);
    public List<AuthorForBook> getAuthorsByAuthorName(String name);
}
