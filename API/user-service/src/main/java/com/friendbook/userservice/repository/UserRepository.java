package com.friendbook.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
