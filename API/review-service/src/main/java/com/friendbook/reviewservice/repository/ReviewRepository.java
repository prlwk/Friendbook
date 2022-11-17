package com.friendbook.reviewservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook.reviewservice.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByBookIdAndUserIdAndDel(Long bookId, Long userId, Boolean del);

    List<Review> getByUserIdAndDel(Long userId, Boolean del);
}
