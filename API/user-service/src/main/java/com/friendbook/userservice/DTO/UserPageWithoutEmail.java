package com.friendbook.userservice.DTO;

import java.util.List;

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
    private List<Object> reviews;
    private List<Object> ratedBooks;
    private List<Object> savingBooks;
    private int countFriends;

    public UserPageWithoutEmail(User user) {
        this.name = user.getName();
        this.login = user.getLogin();
    }
}
