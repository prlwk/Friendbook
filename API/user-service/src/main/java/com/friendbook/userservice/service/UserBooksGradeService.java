package com.friendbook.userservice.service;

import com.friendbook.userservice.model.User;

public interface UserBooksGradeService {
    int getGradeByBookIdAndUser(Long idBook, User user);
}
