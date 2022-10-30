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
public class UserPageWithoutEmail {
    private String name;
    private String login;
    private String image;
    private Set<Book> booksRate;
    private Set<Book> booksWantToRead;
    private Set<Review> reviews;

    public UserPageWithoutEmail(User user) {
        this.name = user.getName();
        this.login = user.getLogin();
        this.booksRate = user.getBooksRate();
        this.booksWantToRead = user.getBooksWantToRead();
        this.reviews = user.getReview();
    }
}
