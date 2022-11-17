package com.friendbook.reviewservice.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.reviewservice.DTO.ReviewForAccount;
import com.friendbook.reviewservice.model.Review;
import com.friendbook.reviewservice.repository.ReviewRepository;
import com.friendbook.reviewservice.service.client.BookRestTemplateClient;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BookRestTemplateClient bookRestTemplateClient;

    @Override
    public Review getReviewByUseridAndBookId(Long userId, Long bookId) {
        Optional<Review> reviewOptional = reviewRepository.findByBookIdAndUserIdAndDel(bookId, userId, false);
        if (reviewOptional.isPresent()) {
            return reviewOptional.get();
        }
        throw new EntityNotFoundException("Review by user with id" + userId + " for book with id " + bookId + " not found");
    }

    @Override
    public void setReview(Long userId, Long bookId, String text) {
        Review review;
        try {
            review = getReviewByUseridAndBookId(userId, bookId);
        } catch (EntityNotFoundException exception) {
            review = new Review();
        }
        review.setDate(Instant.now().toEpochMilli());
        review.setBookId(bookId);
        review.setText(text);
        review.setUserId(userId);
        review.setDel(false);
        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long userId, Long bookId) {
        try {
            Review review = getReviewByUseridAndBookId(userId, bookId);
            review.setDel(true);
            reviewRepository.save(review);
        } catch (EntityNotFoundException ignored) {

        }
    }

    @Override
    public List<ReviewForAccount> getReviewForAccountByUserId(Long userId) {
        List<Review> reviews = reviewRepository.getByUserIdAndDel(userId, false);
        if (reviews == null || reviews.isEmpty()) {
            throw new EntityNotFoundException("Reviews not found");
        }
        reviews.sort(Comparator.comparing(Review::getBookId));
        List<Long> listIdBook = reviews.stream().map(Review::getBookId).toList();
        List<ReviewForAccount> res = bookRestTemplateClient.getRatedBooks(userId, listIdBook);
        if (res == null || res.isEmpty()) {
            throw new EntityNotFoundException("Reviews not found");
        }
        res.sort(Comparator.comparing(ReviewForAccount::getBookId));
        for (int i = 0; i < res.size(); i++) {
            res.get(i).setText(reviews.get(i).getText());
            res.get(i).setDate(reviews.get(i).getDate());
        }
        if (!res.isEmpty()) {
            return res;
        }
        throw new EntityNotFoundException("Reviews not found");
    }
}
