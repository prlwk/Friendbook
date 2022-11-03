package com.src.book.data.remote.service

import com.src.book.data.remote.model.friend.friend.FriendResponse
import com.src.book.data.remote.model.friend.request.FriendRequestResponse
import com.src.book.utils.USER_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendService {
    @GET("$USER_SERVICE_BASE_URL/incoming-requests")
    suspend fun getIncomingRequests(): Response<List<FriendRequestResponse>>

    @GET("$USER_SERVICE_BASE_URL/submit-friend-request")
    suspend fun submitFriendRequest(@Query("friendId") friendId: Long): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/reject-incoming-request")
    suspend fun rejectIncomingFriendRequest(@Query("friendId") friendId: Long): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/outgoing-requests")
    suspend fun getOutgoingRequests(): Response<List<FriendRequestResponse>>

    @GET("$USER_SERVICE_BASE_URL/reject-outgoing-request")
    suspend fun rejectOutgoingFriendRequest(@Query("friendId") friendId: Long): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/send-friend-request")
    suspend fun sendFriendRequest(@Query("login") login: String): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/friends")
    suspend fun getFriends(): Response<List<FriendResponse>>
}