package com.friendbook.userservice.DTO;

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
    private String image;

    public UserForFriends(Long id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
    }
}
