package com.friendbook.bookservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.bookservice.model.UserBooksGrade;

@Repository
public interface UserBooksGradeRepository extends JpaRepository<UserBooksGrade, Long> {

    @Query("SELECT ubg.grade FROM UserBooksGrade ubg WHERE ubg.book.id=:bookId AND ubg.userId=:userId AND ubg.del = false")
    Optional<Integer> getGradeByBookIdAndUserId(Long bookId, Long userId);

    @Query("SELECT ubg FROM UserBooksGrade ubg WHERE ubg.userId=:userId AND ubg.book.id=:bookId AND ubg.del=false")
    Optional<UserBooksGrade> getUserBooksGradeByBookIdAndUserId(Long bookId, Long userId);

    @Query("SELECT ubg.book.id FROM UserBooksGrade ubg WHERE ubg.userId=:userId AND ubg.del=false")
    List<Long> getRatedBooksIdByUserId(Long userId);
}
