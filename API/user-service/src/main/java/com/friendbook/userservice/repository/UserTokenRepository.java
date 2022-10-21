package com.friendbook.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query(value = "DELETE FROM UserToken ut WHERE ut.token=:token and ut.user.id=:id")
    @Modifying
    @Transactional
    void deleteUserTokensByTokenAndUser(String token, Long id);

    List<UserToken> findAllByUser(User user);
}
