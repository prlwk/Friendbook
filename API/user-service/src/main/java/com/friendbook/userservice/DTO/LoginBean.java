package com.friendbook.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginBean {
    private String loginOrEmail;
    private String password;
    private String refreshToken;
    private String accessToken;
    private Boolean isEntryByEmail;
}
