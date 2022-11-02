package com.friendbook.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserBooksGrade;

@Repository
public interface UserBooksGradeRepository extends JpaRepository<UserBooksGrade, Long> {

    Optional<UserBooksGrade> findByBookIdAndUser(Long bookId, User user);
}
