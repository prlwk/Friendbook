package com.friendbook.userservice.DTO;

import org.springframework.core.io.ByteArrayResource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForFriends {
    private Long id;
    private String name;
    private String login;
    private int countRateBooks = 0;
    private int countReviews = 0;
    private int countWantToReadBooks = 0;
    private ByteArrayResource image;

    public UserForFriends(Long id, String name, String login, int countRateBooks, int countReviews, int countWantToReadBooks) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.countRateBooks = countRateBooks;
        this.countReviews = countReviews;
        this.countWantToReadBooks = countWantToReadBooks;
    }
}
