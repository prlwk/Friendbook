package com.friendbook.userservice.service;

import org.springframework.util.MultiValueMap;

import com.friendbook.userservice.DTO.EditUserBean;
import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.model.User;

public interface UserService {
    User registerUser(RegisterBean register);

    boolean isCorrectPassword(User user, String password);

    void changePassword(String password, User user);

    void deleteUser(User user);

    boolean isEmailExist(String email);

    boolean isLoginExist(String login);

    void save(User user);

    void update(User user, EditUserBean editUserBean);

    User findUserByEmail(String email);

    User findUserByLogin(String login);

    void setLinkPhoto(String linkPhoto, Long userId);

    User getUserById(Long id);

    void deleteAllUsersExceptVerified(String email);

    MultiValueMap<String, Object> getInfoForProfile(User user);
}
