package com.friendbook.userservice.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.userservice.DTO.UserForFriends;
import com.friendbook.userservice.DTO.UserInRequest;
import com.friendbook.userservice.model.Friends;
import com.friendbook.userservice.model.User;
import com.friendbook.userservice.repository.FriendsRepository;

@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    FriendsRepository friendsRepository;

    @Autowired
    UserService userService;

    @Override
    public void addFriendsRequest(User sender, User recipient) {
        Friends friends = new Friends();
        friends.setSender(sender);
        friends.setRecipient(recipient);
        friends.setAcceptedRequest(false);
        friendsRepository.save(friends);
    }

    @Override
    public void deleteRequest(User sender, User recipient) {
        Friends friends = friendsRepository.getFriendsBySenderAndRecipient(sender.getId(), recipient.getId());
        if (friends == null || friends.isAcceptedRequest()) {
            throw new EntityNotFoundException("Friend request not found.");
        }
        friendsRepository.delete(friends);
    }

    @Override
    public void submitFriendsRequest(User sender, User recipient) {
        Friends friends = friendsRepository.getFriendsBySenderAndRecipient(sender.getId(), recipient.getId());
        if (friends == null) {
            throw new EntityNotFoundException("Friend request not found.");
        }
        friends.setAcceptedRequest(true);
        friendsRepository.save(friends);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        Friends friends1 = friendsRepository.getFriendsBySenderAndRecipient(user.getId(), friend.getId());
        Friends friends2 = friendsRepository.getFriendsBySenderAndRecipient(friend.getId(), user.getId());
        if (friends1 == null && friends2 == null) {
            throw new EntityNotFoundException("Friends not found.");
        }
        if (friends1 != null) {
            friendsRepository.delete(friends1);
        }
        if (friends2 != null) {
            friendsRepository.delete(friends2);
        }
    }

    @Override
    public List<UserInRequest> getOutgoingRequests(User user) {
        return setImagesForFriendsInRequests(friendsRepository.getOutgoingRequests(user.getId()));
    }

    @Override
    public List<UserInRequest> getIncomingRequests(User user) {
        return setImagesForFriendsInRequests(friendsRepository.getIncomingRequests(user.getId()));
    }

    @Override
    public List<UserForFriends> getFriends(User user) {
        List<UserForFriends> friendsListPart = friendsRepository.getFriendsWhereUserSender(user.getId());
        List<UserForFriends> friendsListPart2 = friendsRepository.getFriendsWhereUserRecipient(user.getId());
        friendsListPart.addAll(friendsListPart2);
        for (UserForFriends userForFriends : friendsListPart) {
            userForFriends.setImage("/user/image?id=" + userForFriends.getId());
        }
        return friendsListPart;
    }

    @Override
    public boolean isFriends(User user1, User user2) {
        Friends friends = friendsRepository.getFriendsBySenderAndRecipient(user1.getId(), user2.getId());
        if (friends != null && friends.isAcceptedRequest()) {
            return true;
        }
        friends = friendsRepository.getFriendsBySenderAndRecipient(user2.getId(), user1.getId());
        if (friends != null && friends.isAcceptedRequest()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistingRequest(User user1, User user2) {
        Friends friends = friendsRepository.getFriendsBySenderAndRecipient(user1.getId(), user2.getId());
        if (friends != null && !friends.isAcceptedRequest()) {
            return true;
        }
        return false;
    }

    @Override
    public int getCountIncomingRequest(User user) {
        return friendsRepository.getCountIncomingRequests(user.getId());
    }

    private List<UserInRequest> setImagesForFriendsInRequests(List<UserInRequest> userInRequestList) {
        for (UserInRequest userInRequest : userInRequestList) {
            userInRequest.setImage("/user/image?id=" + userInRequest.getId());
        }
        return userInRequestList;
    }
}
