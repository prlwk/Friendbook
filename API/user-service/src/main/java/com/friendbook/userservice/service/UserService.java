package com.friendbook.userservice.service;

import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.model.User;

public interface UserService {
    User registerUser(RegisterBean register);

    void changePassword(String password, User user);

    void deleteUser(User user);

    boolean isEmailExist(String email);

    boolean isLoginExist(String login);

    void save(User user);

    User findUserByEmail(String email);

    User findUserByLogin(String login);

    void setLinkPhoto(String linkPhoto, Long userId);

    User getUserById(Long id);
}
