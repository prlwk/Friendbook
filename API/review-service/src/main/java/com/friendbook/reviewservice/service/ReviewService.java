package com.friendbook.reviewservice.service;

import java.util.List;

import com.friendbook.reviewservice.DTO.ReviewForAccount;
import com.friendbook.reviewservice.model.Review;

public interface ReviewService {
    Review getReviewByUseridAndBookId(Long userId, Long bookId);

    void setReview(Long userId, Long bookId, String text);

    void deleteReview(Long userId, Long bookId);

    List<ReviewForAccount> getReviewForAccountByUserId(Long userId);
}
