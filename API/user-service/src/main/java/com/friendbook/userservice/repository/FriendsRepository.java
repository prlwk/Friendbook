package com.friendbook.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.userservice.DTO.UserForFriends;
import com.friendbook.userservice.DTO.UserInRequest;
import com.friendbook.userservice.model.Friends;


@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    @Query("SELECT new com.friendbook.userservice.DTO.UserForFriends(f.recipient.id," +
            " f.recipient.name, f.recipient.login, size(f.recipient.booksRate), size(f.recipient.review)," +
            " size(f.recipient.booksWantToRead)) " +
            "FROM Friends f " +
            "WHERE f.sender.id=:id AND f.isAcceptedRequest = true ")
    List<UserForFriends> getFriendsWhereUserSender(Long id);

    @Query("SELECT new com.friendbook.userservice.DTO.UserForFriends(f.sender.id," +
            " f.sender.name, f.sender.login, size(f.sender.booksRate), size(f.sender.review)," +
            " size(f.sender.booksWantToRead)) " +
            "FROM Friends f " +
            "WHERE f.recipient.id=:id AND f.isAcceptedRequest = true")
    List<UserForFriends> getFriendsWhereUserRecipient(Long id);

    @Query("SELECT f " +
            "FROM Friends f " +
            "WHERE f.sender.id=:senderId AND f.recipient.id=:recipientId")
    Friends getFriendsBySenderAndRecipient(Long senderId, Long recipientId);

    @Query("SELECT new com.friendbook.userservice.DTO.UserInRequest(f.recipient.id," +
            " f.recipient.name, f.recipient.login) " +
            "FROM Friends f " +
            "WHERE f.sender.id=:id AND f.isAcceptedRequest = false")
    List<UserInRequest> getOutgoingRequests(Long id);

    @Query("SELECT new com.friendbook.userservice.DTO.UserInRequest(f.sender.id," +
            " f.sender.name, f.sender.login) " +
            "FROM Friends f " +
            "WHERE f.recipient.id=:id AND f.isAcceptedRequest = false")
    List<UserInRequest> getIncomingRequests(Long id);

    @Query("SELECT count(f) " +
            "FROM Friends f " +
            "WHERE f.recipient.id=:id AND f.isAcceptedRequest = false")
    int getCountIncomingRequests(Long id);
}
