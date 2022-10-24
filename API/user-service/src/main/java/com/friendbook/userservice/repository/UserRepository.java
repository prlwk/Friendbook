package com.friendbook.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByLogin(String login);

    List<User> findUsersByEmail(String email);

    @Query(value = "UPDATE User u SET u.linkPhoto=:linkPhoto WHERE u.id=:userId")
    @Modifying
    @Transactional
    void updateLinkPhotoByUserId(String linkPhoto, Long userId);

    @Query(value = "DELETE FROM User u WHERE u.email=:email and u.isEnabled=false")
    @Modifying
    @Transactional
    void deleteAllUsersExceptVerified(String email);
}
