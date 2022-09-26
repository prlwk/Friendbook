package com.friendbook.service;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorWithBooks;

public interface AuthorService {
    public AuthorWithBooks getAuthor(Long authorId);
    public AuthorForBook getAuthorForBook(Long authorId);
}
