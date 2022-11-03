package com.friendbook.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.userservice.model.UserBooksWantToRead;

@Repository
public interface UserBooksWantToReadRepository extends JpaRepository<UserBooksWantToRead, Long> {

    @Query("SELECT count(ubw)>0 FROM UserBooksWantToRead ubw WHERE ubw.user.id=:userId AND ubw.bookId=:bookId")
    boolean getSavingByBookIdAndUserId(Long bookId, Long userId);
}
