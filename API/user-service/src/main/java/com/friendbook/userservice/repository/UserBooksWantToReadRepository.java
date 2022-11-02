package com.friendbook.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserBooksWantToRead;

@Repository
public interface UserBooksWantToReadRepository extends JpaRepository<UserBooksWantToRead, Long> {

    Optional<UserBooksWantToRead> findByBookIdAndUser(Long bookId, User user);
}
