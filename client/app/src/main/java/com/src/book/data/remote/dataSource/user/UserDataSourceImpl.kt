package com.src.book.data.remote.dataSource.user

import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.service.UserServiceWithToken
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.utils.ALREADY_FRIENDS
import com.src.book.data.remote.utils.ErrorMessage
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.SendFriendRequestState

class UserDataSourceImpl(
    private val userService: UserService,
    private val userServiceWithToken: UserServiceWithToken,
    private val sessionStorage: SessionStorage
) :
    UserDataSource {
    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): ChangePasswordState {
        val response = userServiceWithToken.changePassword(
            refreshToken = sessionStorage.getRefreshToken(),
            oldPassword = oldPassword,
            newPassword = newPassword
        )
        if (response.isSuccessful) {
            return ChangePasswordState.SuccessState
        }
        if (response.code() == 404) {
            return ChangePasswordState.WrongPasswordState
        }
        return ChangePasswordState.ErrorState
    }

    override suspend fun logout(): BasicState {
        val refreshToken = sessionStorage.getRefreshToken()
        val response = userServiceWithToken.logout(refreshToken)
        return if (response.isSuccessful) {
            sessionStorage.clearSession()
            BasicState.SuccessState
        } else {
            BasicState.ErrorState
        }
    }

    override suspend fun sendFriendRequest(login: String): SendFriendRequestState {
        val response = userServiceWithToken.sendFriendRequest(login)
        if (response.isSuccessful) {
            return SendFriendRequestState.SuccessState
        }
        when (response.code()) {
            404 -> {
                return SendFriendRequestState.ErrorLoginState
            }
            409 -> {
                val errorMessage = ErrorMessage<Unit>()
                val message = errorMessage.getErrorMessage(response)
                if (message == ALREADY_FRIENDS) {
                    return SendFriendRequestState.FriendAlreadyExists
                }
                return SendFriendRequestState.SuchRequestAlreadyExists
            }
        }
        return SendFriendRequestState.ErrorState
    }
}
