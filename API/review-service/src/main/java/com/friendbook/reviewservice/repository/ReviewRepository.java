package com.friendbook.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook.reviewservice.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
