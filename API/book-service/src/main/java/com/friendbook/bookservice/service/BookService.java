package com.friendbook.bookservice.service;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;

public interface BookService {
    Book getBookById(Long id);

    BookForBookPage getBookForBookPageById(Long id);

    Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId);

    Set<BookForSearch> getBooksByAuthorId(Long authorId, Boolean isAuthUser, HttpServletRequest request);

    List<BookForSearch> getBooksBySearch(int numberPage,
                                         int sizePage,
                                         com.friendbook.bookservice.utils.Sort sort,
                                         String word,
                                         int startRating,
                                         int finishRating,
                                         List<Long> listTags,
                                         List<Long> listGenres,
                                         List<Long> listId);
}
