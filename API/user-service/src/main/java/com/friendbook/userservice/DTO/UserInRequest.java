package com.friendbook.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInRequest {
    private Long id;
    private String name;
    private String login;
    private String image;

    public UserInRequest(Long id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
    }
}
