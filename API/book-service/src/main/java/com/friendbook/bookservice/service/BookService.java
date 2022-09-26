package com.friendbook.bookservice.service;


import java.util.Set;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;

public interface BookService {
    public Book getBookById(Long id);
    public Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId);
    public Set<BookForSearch> getBooksByAuthorId(Long authorId);
}
