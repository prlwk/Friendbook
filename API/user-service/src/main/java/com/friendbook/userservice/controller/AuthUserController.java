package com.friendbook.userservice.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendbook.userservice.DTO.EditUserBean;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.security.jwt.JwtService;
import com.friendbook.userservice.service.RefreshTokenService;
import com.friendbook.userservice.service.UserBooksGradeService;
import com.friendbook.userservice.service.UserBooksWantToReadService;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.UserTokenService;
import com.friendbook.userservice.utils.AppError;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserBooksGradeService userBooksGradeService;

    @Autowired
    private UserBooksWantToReadService userBooksWantToReadService;

    @RequestMapping(path = "/check-password", method = RequestMethod.GET)
    public ResponseEntity<?> checkPassword(@RequestParam String password, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        Map<String, Boolean> map = new HashMap<>();
        map.put("isCorrectPassword", userService.isCorrectPassword(user, password));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.GET)
    public ResponseEntity<?> changePassword(@RequestParam String newPassword,
                                            @RequestParam(required = false) String oldPassword,
                                            @RequestParam String refreshToken,
                                            HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        if (oldPassword != null) {
            if (!userService.isCorrectPassword(user, oldPassword)) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Wrong old password!"), HttpStatus.NOT_FOUND);
            }
        }
        userTokenService.deleteAllAccessTokensByUser(user);
        refreshTokenService.deleteAllRefreshTokensByUser(user);
        userTokenService.addAccessToken(user, resolveToken(request));
        refreshTokenService.addRefreshToken(user, refreshToken);
        userService.changePassword(newPassword, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        return new ResponseEntity<>(userService.getInfoForProfile(user), HttpStatus.OK);
    }

    @RequestMapping(path = "/edit-profile", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> editProfile(@RequestPart String editUserBeanString,
                                         @RequestPart(required = false) MultipartFile file,
                                         HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        EditUserBean editUserBean;
        try {
            editUserBean = mapper.readValue(editUserBeanString, EditUserBean.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert JSON object"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!user.getLogin().equals(editUserBean.getLogin())) {
            if (userService.isLoginExist(editUserBean.getLogin())) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.CONFLICT.value(),
                                "This login is already in use"), HttpStatus.CONFLICT);
            }
        }
        int photoVersion = 0;
        try {
            if (file != null) {
                String oldLink = user.getLinkPhoto();
                Scanner sc = new Scanner(oldLink);
                sc.useDelimiter("\\.");
                if (sc.hasNextInt()) {
                    sc.nextInt();
                    photoVersion = sc.nextInt();
                }
                photoVersion++;
                String path = new File("").getAbsolutePath();
                File newFile = new File(path + user.getId() + "." + photoVersion + ".jpg");
                file.transferTo(newFile);
                File oldPhoto = new File(path + user.getId() + "." + --photoVersion + ".jpg");
                oldPhoto.delete();
            }
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert photo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userService.update(user, editUserBean);
        if (file != null) {
            userService.setLinkPhoto(user.getId() + "." + ++photoVersion + ".jpg", user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(@RequestParam String refreshToken,
                                    HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        userTokenService.deleteAccessToken(user, resolveToken(request));
        refreshTokenService.deleteRefreshToken(user, refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-account", method = RequestMethod.GET)
    public ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        String path = new File("").getAbsolutePath();
        File file = new File(path + user.getLinkPhoto());
        file.delete();
        userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/grade", method = RequestMethod.GET)
    public ResponseEntity<?> getGrade(@RequestParam Long idBook, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        try {
            return new ResponseEntity<>(userBooksGradeService.getGradeByBookIdAndUser(idBook, user),
                    HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Grade not found"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/is-saving-book", method = RequestMethod.GET)
    public ResponseEntity<?> isSavingBook(@RequestParam Long idBook, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        return new ResponseEntity<>(userBooksWantToReadService.isSavingBook(idBook, user), HttpStatus.OK);
    }

    @RequestMapping(path = "/check-token", method = RequestMethod.GET)
    public ResponseEntity<?> checkToken(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private ResponseEntity<?> getUserByRequest(HttpServletRequest request) {
        String email;
        String token = resolveToken(request);
        try {
            email = jwtService.extractUserInfo(token);
        } catch (URISyntaxException | IOException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Extracting user info by token is failed."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user;
        try {
            user = userService.findUserByEmail(email);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email " + email + " does not exist."), HttpStatus.NOT_FOUND);
        }
        if (!userTokenService.isCorrectAccessToken(user, token)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "This access token does not exist."), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
