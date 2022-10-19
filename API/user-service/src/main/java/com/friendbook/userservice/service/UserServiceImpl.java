package com.friendbook.userservice.service;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.model.RefreshToken;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.model.UserToken;
import com.friendbook.userservice.repository.RefreshTokenRepository;
import com.friendbook.userservice.repository.UserRepository;
import com.friendbook.userservice.repository.UserTokenRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTokenRepository userTokenRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterBean register) {
        User newUser = new User();
        newUser.setLogin(register.getLogin());
        newUser.setName(register.getName());
        newUser.setEmail(register.getEmail());
        newUser.setPassword(passwordEncoder.encode(register.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public void changePassword(String password, User user) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

    @Override
    public boolean isEmailExist(String email) {
        List<User> users = userRepository.findUsersByEmail(email);
        for (User user : users) {
            if (user.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLoginExist(String login) {
        List<User> users = userRepository.findUsersByLogin(login);
        for (User user : users) {
            if (user.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        List<User> users = userRepository.findUsersByEmail(email);
        for (User u : users) {
            if (u.isEnabled()) {
                return u;
            }
        }
        throw new EntityNotFoundException("User not found.");
    }

    @Override
    public User findUserByLogin(String login) {
        List<User> users = userRepository.findUsersByLogin(login);
        for (User u : users) {
            if (u.isEnabled()) {
                return u;
            }
        }
        throw new EntityNotFoundException("User not found.");
    }

    @Override
    public void setLinkPhoto(String linkPhoto, Long userId) {
        userRepository.updateLinkPhotoByUserId(linkPhoto, userId);
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

    @Override
    public boolean updateRefreshToken(User user, String newToken, String oldToken) {
        Set<RefreshToken> tokenSet = user.getRefreshTokens();
        for (RefreshToken refreshToken : tokenSet) {
            if (refreshToken.getToken().equals(oldToken)) {
                RefreshToken token = new RefreshToken();
                token.setToken(newToken);
                token.setUser(user);
                refreshTokenRepository.deleteRefreshTokensByTokenAndUser(oldToken, user.getId());
                refreshTokenRepository.save(token);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addToken(User user, String newToken) {
        UserToken userToken = new UserToken();
        userToken.setToken(newToken);
        userToken.setUser(user);
        userTokenRepository.save(userToken);
    }

    @Override
    public void addRefreshToken(User user, String newToken) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(newToken);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteToken(User user, String token) {
        userTokenRepository.deleteUserTokensByTokenAndUser(token, user.getId());
    }

    @Override
    public void deleteRefreshToken(User user, String token) {
        refreshTokenRepository.deleteRefreshTokensByTokenAndUser(token, user.getId());
    }
}
