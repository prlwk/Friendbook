package com.friendbook.userservice.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.userservice.DTO.UserForFriends;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.security.jwt.JwtService;
import com.friendbook.userservice.service.FriendsService;
import com.friendbook.userservice.service.UserService;
import com.friendbook.userservice.service.UserTokenService;
import com.friendbook.userservice.utils.AppError;

@RestController
public class FriendsController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    FriendsService friendsService;

    @Autowired
    private UserTokenService userTokenService;

    @RequestMapping(path = "/send-friend-request", method = RequestMethod.GET)
    public ResponseEntity<?> sendFriendRequest(@RequestParam String login, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        User friend;
        try {
            friend = userService.findUserByLogin(login);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with login " + login + " does not exist."), HttpStatus.NOT_FOUND);
        }
        if (friendsService.isFriends(user, friend)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.CONFLICT.value(),
                            "They are already friends."), HttpStatus.CONFLICT);
        }
        if (friendsService.isExistingRequest(user, friend)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.CONFLICT.value(),
                            "Such request already exists"), HttpStatus.CONFLICT);
        }
        if (friendsService.isExistingRequest(friend, user)) {
            friendsService.submitFriendsRequest(friend, user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        friendsService.addFriendsRequest(user, friend);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/submit-friend-request", method = RequestMethod.GET)
    public ResponseEntity<?> submitFriendRequest(@RequestParam Long friendId, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        User friend;
        try {
            friend = userService.getUserById(friendId);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + friendId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        try {
            friendsService.submitFriendsRequest(friend, user);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Friend request does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/friends", method = RequestMethod.GET)
    public ResponseEntity<?> getFriends(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        List<UserForFriends> list = friendsService.getFriends(user);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(path = "/outgoing-requests", method = RequestMethod.GET)
    public ResponseEntity<?> getOutgoingRequests(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        return new ResponseEntity<>(friendsService.getOutgoingRequests(user), HttpStatus.OK);
    }

    @RequestMapping(path = "/incoming-requests", method = RequestMethod.GET)
    public ResponseEntity<?> getIncomingRequests(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        return new ResponseEntity<>(friendsService.getIncomingRequests(user), HttpStatus.OK);
    }

    @RequestMapping(path = "/reject-outgoing-request", method = RequestMethod.GET)
    public ResponseEntity<?> rejectOutgoingRequest(@RequestParam Long friendId, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        User friend;
        try {
            friend = userService.getUserById(friendId);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + friendId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        try {
            friendsService.deleteRequest(user, friend);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Friend request does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/reject-incoming-request", method = RequestMethod.GET)
    public ResponseEntity<?> rejectIncomingRequest(@RequestParam Long friendId, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        User friend;
        try {
            friend = userService.getUserById(friendId);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + friendId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        try {
            friendsService.deleteRequest(friend, user);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Friend request does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-friend", method = RequestMethod.GET)
    public ResponseEntity<?> deleteFriend(@RequestParam Long friendId, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        User friend;
        try {
            friend = userService.getUserById(friendId);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with id " + friendId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        try {
            friendsService.deleteFriend(user, friend);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Friends does not exist."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/count-incoming-requests", method = RequestMethod.GET)
    public ResponseEntity<?> getCountsFriendRequests(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        return new ResponseEntity<>(friendsService.getCountIncomingRequest(user), HttpStatus.OK);
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
