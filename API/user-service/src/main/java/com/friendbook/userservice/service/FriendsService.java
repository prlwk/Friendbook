package com.friendbook.userservice.service;

import java.util.List;

import com.friendbook.userservice.DTO.UserForFriends;
import com.friendbook.userservice.DTO.UserInRequest;
import com.friendbook.userservice.model.User;

public interface FriendsService {
    void addFriendsRequest(User sender, User recipient);
    void deleteRequest(User sender, User recipient);
    void submitFriendsRequest(User sender, User recipient);
    void deleteFriend(User user, User friend);
    List<UserInRequest> getOutgoingRequests(User user);
    List<UserInRequest> getIncomingRequests(User user);
    List<UserForFriends> getFriends(User user);
    boolean isFriends(User user1, User user2);
    boolean isExistingRequest(User user1, User user2);
}
