package com.friendbook.userservice.service;

import com.friendbook.userservice.model.User;

public interface RefreshTokenService {
    void addRefreshToken(User user, String newToken);

    boolean updateRefreshToken(User user, String newToken, String oldToken);

    void deleteRefreshToken(User user, String newToken);

    void deleteAllRefreshTokensByUser(User user);

    boolean isCorrectRefreshToken(User user, String token);

    Long getExpirationTime();

}
