package com.friendbook.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.userservice.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "DELETE FROM RefreshToken r WHERE r.token=:token and r.user.id=:userId")
    @Modifying
    @Transactional
    void deleteRefreshTokensByTokenAndUser(String token, Long userId);
}
