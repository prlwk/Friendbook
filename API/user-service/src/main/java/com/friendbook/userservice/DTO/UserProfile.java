package com.friendbook.userservice.DTO;

import java.util.Set;

import com.friendbook.userservice.model.Book;
import com.friendbook.userservice.model.Review;
import com.friendbook.userservice.model.User;

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
    private Set<Book> booksRate;
    private Set<Book> booksWantToRead;
    private Set<Review> reviews;

    public UserProfile(User user) {
        this.name = user.getName();
        this.login = user.getLogin();
        this.booksRate = user.getBooksRate();
        this.booksWantToRead = user.getBooksWantToRead();
        this.reviews = user.getReview();
    }
}
