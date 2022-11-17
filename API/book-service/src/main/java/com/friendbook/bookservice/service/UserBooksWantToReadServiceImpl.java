package com.friendbook.bookservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.model.UserBooksWantToRead;
import com.friendbook.bookservice.repository.UserBooksWantToReadRepository;

@Service
public class UserBooksWantToReadServiceImpl implements UserBooksWantToReadService {
    @Autowired
    UserBooksWantToReadRepository userBooksWantToReadRepository;

    @Override
    public Boolean isSavingBook(Long bookId, Long userId) {
        return userBooksWantToReadRepository.getSavingBookByBookIdAndUserId(bookId, userId);
    }

    @Override
    public void saveBook(Book book, Long userId) {
        UserBooksWantToRead userBooksWantToRead = new UserBooksWantToRead();
        userBooksWantToRead.setUserId(userId);
        userBooksWantToRead.setBook(book);
        userBooksWantToRead.setDel(false);
        userBooksWantToReadRepository.save(userBooksWantToRead);
    }

    @Override
    public void deleteSavingBook(Book book, Long userId) {
        Optional<UserBooksWantToRead> userBooksWantToReadOptional = userBooksWantToReadRepository.getUserBooksWantToReadByBookAndUserId(book, userId);
        if (userBooksWantToReadOptional.isPresent() && !userBooksWantToReadOptional.get().isDel()) {
            UserBooksWantToRead userBooksWantToRead = userBooksWantToReadOptional.get();
            userBooksWantToRead.setDel(true);
            userBooksWantToReadRepository.save(userBooksWantToRead);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " does not save book with id " + book.getId());
        }
    }

    @Override
    public List<Long> getSavingBooksIdByUserId(Long userId) {
        return userBooksWantToReadRepository.getSavingBooksIdByUserId(userId);
    }
}
