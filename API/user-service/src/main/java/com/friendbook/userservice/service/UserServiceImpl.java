package com.friendbook.userservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.friendbook.userservice.DTO.EditUserBean;
import com.friendbook.userservice.DTO.RegisterBean;
import com.friendbook.userservice.DTO.UserProfile;
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
    public boolean isCorrectPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
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
    public void update(User user, EditUserBean editUserBean) {
        user.setName(editUserBean.getName());
        user.setLogin(editUserBean.getLogin());
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

    @Override
    public void deleteAllUsersExceptVerified(String email) {
        userRepository.deleteAllUsersExceptVerified(email);
    }

    @Override
    public MultiValueMap<String, Object> getInfoForProfile(User user) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("user", new UserProfile(user));
        try {
            String path = new File("").getAbsolutePath();
            File file = new File(path + "/user-service/src/main/resources/user-photo/" + user.getLinkPhoto());
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            formData.add("image", new ByteArrayResource(multipartFile.getBytes()));
        } catch (IOException e) {
            formData.add("image", null);
        }
        return formData;
    }
}
