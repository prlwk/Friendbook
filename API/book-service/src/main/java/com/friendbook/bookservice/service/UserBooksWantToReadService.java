package com.friendbook.bookservice.service;

import java.util.List;

import com.friendbook.bookservice.model.Book;

public interface UserBooksWantToReadService {
    Boolean isSavingBook(Long bookId, Long userId);

    void saveBook(Book book, Long userId);

    void deleteSavingBook(Book book, Long userId);

    List<Long> getSavingBooksIdByUserId(Long userId);
}
