package com.friendbook.bookservice.service;

import java.util.List;

import com.friendbook.bookservice.model.Book;

public interface UserBooksGradeService {
    int getGradeByBookIdAndUserId(Long bookId, Long userId);

    void setGrade(Book book, Integer grade, Long userId);

    List<Long> getRatedBooksIdByUserId(Long userId);
}
