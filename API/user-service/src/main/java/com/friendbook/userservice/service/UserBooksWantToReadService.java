package com.friendbook.userservice.service;

import com.friendbook.userservice.model.User;

public interface UserBooksWantToReadService {
    Boolean isSavingBook(Long idBook, User user);

    void saveBook(Long idBook, User user);

    void deleteSavingBook(Long idBook, User user);
}
