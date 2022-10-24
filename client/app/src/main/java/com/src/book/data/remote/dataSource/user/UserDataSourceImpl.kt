package com.src.book.data.remote.dataSource.user

import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.service.UserServiceWithToken
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState

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
}
