package com.friendbook.userservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

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
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new EntityNotFoundException("User not found.");
    }
}
