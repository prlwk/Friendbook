package com.friendbook.userservice.service;

import com.friendbook.userservice.model.User;

public interface UserTokenService {
    void addAccessToken(User user, String newToken);

    boolean updateAccessToken(User user, String newToken, String oldToken);

    void deleteAccessToken(User user, String newToken);

    void deleteAllAccessTokensByUser(User user);

    boolean isCorrectAccessToken(User user, String token);

    Long getExpirationTime();
}
