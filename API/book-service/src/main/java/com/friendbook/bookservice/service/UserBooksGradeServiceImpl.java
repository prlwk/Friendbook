package com.friendbook.bookservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.model.UserBooksGrade;
import com.friendbook.bookservice.repository.UserBooksGradeRepository;

@Service
public class UserBooksGradeServiceImpl implements UserBooksGradeService {
    @Autowired
    UserBooksGradeRepository userBooksGradeRepository;

    public int getGradeByBookIdAndUserId(Long bookId, Long userId) {
        Optional<Integer> grade = userBooksGradeRepository.getGradeByBookIdAndUserId(bookId, userId);
        if (grade.isPresent()) {
            return grade.get();
        }
        throw new EntityNotFoundException("Grade not found.");
    }

    @Override
    public void setGrade(Book book, Integer grade, Long userId) {
        Optional<UserBooksGrade> userBooksGradeOptional = userBooksGradeRepository.getUserBooksGradeByBookIdAndUserId(book.getId(), userId);
        if (userBooksGradeOptional.isEmpty() && grade == null) {
            return;
        }
        UserBooksGrade userBooksGrade = userBooksGradeOptional.orElseGet(UserBooksGrade::new);
        userBooksGrade.setGrade(grade);
        userBooksGrade.setUserId(userId);
        userBooksGrade.setBook(book);
        userBooksGrade.setDel(grade == null);
        userBooksGradeRepository.save(userBooksGrade);
    }

    @Override
    public List<Long> getRatedBooksIdByUserId(Long userId) {
        return userBooksGradeRepository.getRatedBooksIdByUserId(userId);
    }
}
