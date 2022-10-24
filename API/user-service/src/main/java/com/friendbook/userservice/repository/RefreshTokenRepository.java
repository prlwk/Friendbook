package com.friendbook.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.userservice.model.RefreshToken;
import com.friendbook.userservice.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "DELETE FROM RefreshToken r WHERE r.token=:token and r.user.id=:userId")
    @Modifying
    @Transactional
    void deleteRefreshTokensByTokenAndUser(String token, Long userId);

    @Query(value = "DELETE FROM RefreshToken r WHERE r.user.id=:id")
    @Modifying
    @Transactional
    void deleteAllRefreshTokensByUser(Long id);

    List<RefreshToken> findAllByUser(User user);
}
