package com.friendbook.userservice.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserToken;
import com.friendbook.userservice.repository.UserTokenRepository;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    UserTokenRepository userTokenRepository;

    final public int ACCESS_TOKEN_EXPIRATION_TIME_MINUTES = 30;

    @Override
    public void addAccessToken(User user, String newToken) {
        UserToken userToken = new UserToken();
        userToken.setToken(newToken);
        userToken.setUser(user);
        userTokenRepository.save(userToken);
    }

    @Override
    public void deleteAccessToken(User user, String token) {
        userTokenRepository.deleteUserTokensByTokenAndUser(token, user.getId());
    }

    @Override
    public boolean isCorrectAccessToken(User user, String token) {
        List<UserToken> list = userTokenRepository.findAllByUser(user);
        for (UserToken userToken : list) {
            if (userToken.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long getExpirationTime() {
        Calendar today = Calendar.getInstance();
        Calendar sixMonthsAhead = Calendar.getInstance();
        sixMonthsAhead.add(Calendar.MINUTE, ACCESS_TOKEN_EXPIRATION_TIME_MINUTES);
        return sixMonthsAhead.getTimeInMillis() - today.getTimeInMillis() - 30000;
    }

    @Override
    public boolean updateAccessToken(User user, String newToken, String oldToken) {
        Set<UserToken> tokenSet = user.getUserTokens();
        for (UserToken userToken : tokenSet) {
            if (userToken.getToken().equals(oldToken)) {
                UserToken newUserToken = new UserToken();
                newUserToken.setToken(newToken);
                newUserToken.setUser(user);
                userTokenRepository.deleteUserTokensByTokenAndUser(oldToken, user.getId());
                userTokenRepository.save(newUserToken);
                return true;
            }
        }
        return false;
    }
}
