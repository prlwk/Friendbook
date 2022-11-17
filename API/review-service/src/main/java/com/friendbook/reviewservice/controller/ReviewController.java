package com.friendbook.reviewservice.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.friendbook.reviewservice.DTO.ReviewForAccount;
import com.friendbook.reviewservice.service.ReviewService;
import com.friendbook.reviewservice.service.client.BookRestTemplateClient;
import com.friendbook.reviewservice.service.client.UserRestTemplateClient;
import com.friendbook.reviewservice.utils.AppError;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRestTemplateClient userRestTemplateClient;

    @Autowired
    private BookRestTemplateClient bookRestTemplateClient;

    @RequestMapping(path = "/set-review", method = RequestMethod.GET)
    public ResponseEntity<?> setReview(@RequestParam String text,
                                       @RequestParam long idBook,
                                       HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can write review"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }
        try {
            if (bookRestTemplateClient.checkGradeExists(idBook, request)) {
                reviewService.setReview(userId, idBook, text);
            }
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Lost connection with book service"), HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can have grade"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/reviews-for-user", method = RequestMethod.GET)
    public ResponseEntity<?> getReviewsForUser(Long userId) {
        try {
            List<ReviewForAccount> reviews = reviewService.getReviewForAccountByUserId(userId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/delete-review", method = RequestMethod.GET)
    public ResponseEntity<?> deleteReview(@RequestParam long idBook, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can write review"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }
        reviewService.deleteReview(userId, idBook);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
