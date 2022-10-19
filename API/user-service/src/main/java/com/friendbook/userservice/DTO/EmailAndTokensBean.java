package com.friendbook.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAndTokensBean {
    private String login;
    private String refreshToken;
    private String accessToken;
}
