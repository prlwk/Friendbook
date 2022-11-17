package com.friendbook.bookservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.model.UserBooksWantToRead;

@Repository
public interface UserBooksWantToReadRepository extends JpaRepository<UserBooksWantToRead, Long> {

    @Query("SELECT count(ubw)>0 FROM UserBooksWantToRead ubw WHERE ubw.userId=:userId AND ubw.book.id=:bookId AND ubw.del=false")
    boolean getSavingBookByBookIdAndUserId(Long bookId, Long userId);

    Optional<UserBooksWantToRead> getUserBooksWantToReadByBookAndUserIdAndDel(Book book, Long userId, Boolean del);

    @Query("SELECT ubw.book.id FROM UserBooksWantToRead ubw WHERE ubw.userId=:userId AND ubw.del=false")
    List<Long> getSavingBooksIdByUserId(Long userId);
}
