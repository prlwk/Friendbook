package com.friendbook.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.repository.UserBooksWantToReadRepository;

@Service
public class UserBooksWantToReadServiceImpl implements UserBooksWantToReadService {
    @Autowired
    UserBooksWantToReadRepository userBooksWantToReadRepository;

    @Override
    public Boolean isSavingBook(Long idBook, User user) {
        if (userBooksWantToReadRepository.findByBookIdAndUser(idBook, user).isPresent()) {
            return true;
        }
        return false;
    }
}
