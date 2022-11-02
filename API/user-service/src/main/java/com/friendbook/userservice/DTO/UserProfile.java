package com.friendbook.userservice.DTO;

import java.util.Set;

import com.friendbook.userservice.model.Review;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserBooksGrade;
import com.friendbook.userservice.model.UserBooksWantToRead;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String name;
    private String login;
    private String image;
    private String email;
    private Set<UserBooksGrade> booksRate;
    private Set<UserBooksWantToRead> booksWantToRead;
    private Set<Review> reviews;

    public UserProfile(User user) {
        this.name = user.getName();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.booksRate = user.getBooksRate();
        this.booksWantToRead = user.getBooksWantToRead();
        this.reviews = user.getReview();
    }
}
