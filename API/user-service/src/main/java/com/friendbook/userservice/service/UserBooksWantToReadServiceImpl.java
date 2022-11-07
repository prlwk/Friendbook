package com.friendbook.userservice.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserBooksWantToRead;
import com.friendbook.userservice.repository.UserBooksWantToReadRepository;

@Service
public class UserBooksWantToReadServiceImpl implements UserBooksWantToReadService {
    @Autowired
    UserBooksWantToReadRepository userBooksWantToReadRepository;

    @Override
    public Boolean isSavingBook(Long idBook, User user) {
        return userBooksWantToReadRepository.getSavingByBookIdAndUserId(idBook, user.getId());
    }

    @Override
    public void saveBook(Long idBook, User user) {
        UserBooksWantToRead userBooksWantToRead = new UserBooksWantToRead();
        userBooksWantToRead.setUser(user);
        userBooksWantToRead.setBookId(idBook);
        userBooksWantToReadRepository.save(userBooksWantToRead);
    }

    @Override
    public void deleteSavingBook(Long idBook, User user) {
        Optional<UserBooksWantToRead> userBooksWantToReadOptional = userBooksWantToReadRepository.getUserBooksWantToReadByBookIdAndAndUser(idBook, user);
        if (userBooksWantToReadOptional.isPresent()) {
            userBooksWantToReadRepository.delete(userBooksWantToReadOptional.get());
        } else {
            throw new EntityNotFoundException("User with id " + user.getId() + " does not save book with id " + idBook);
        }
    }
}
