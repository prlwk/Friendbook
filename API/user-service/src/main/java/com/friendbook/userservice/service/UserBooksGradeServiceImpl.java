package com.friendbook.userservice.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserBooksGrade;
import com.friendbook.userservice.repository.UserBooksGradeRepository;

@Service
public class UserBooksGradeServiceImpl implements UserBooksGradeService {
    @Autowired
    UserBooksGradeRepository userBooksGradeRepository;

    public int getGradeByBookIdAndUser(Long idBook, User user) {
        Optional<UserBooksGrade> userBooksGrade = userBooksGradeRepository.findByBookIdAndUser(idBook, user);
        if (userBooksGrade.isPresent()) {
            return userBooksGrade.get().getGrade();
        }
        throw new EntityNotFoundException("Grade not found.");
    }
}
