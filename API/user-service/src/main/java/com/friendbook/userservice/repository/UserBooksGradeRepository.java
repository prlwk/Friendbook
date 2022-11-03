package com.friendbook.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.userservice.model.UserBooksGrade;

@Repository
public interface UserBooksGradeRepository extends JpaRepository<UserBooksGrade, Long> {

    @Query("SELECT ubg.grade FROM UserBooksGrade ubg WHERE ubg.user.id=:userId AND ubg.bookId=:bookId")
    Optional<Integer> getGradeByBookIdAndUserId(Long bookId, Long userId);
}
