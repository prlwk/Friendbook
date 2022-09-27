package com.friendbook.bookservice.service;


import java.util.Set;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;

public interface BookService {
    public BookForBookPage getBookById(Long id);
    public Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId);
    public Set<BookForSearch> getBooksByAuthorId(Long authorId);
}
