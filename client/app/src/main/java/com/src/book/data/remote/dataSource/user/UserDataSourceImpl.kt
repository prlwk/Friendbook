package com.src.book.data.remote.dataSource.user

import com.src.book.data.remote.model.user.changePassword.ChangePasswordResponse
import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.service.UserServiceWithToken
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.utils.BasicState

class UserDataSourceImpl(
    private val userService: UserService,
    private val userServiceWithToken: UserServiceWithToken,
    private val sessionStorage: SessionStorage
) :
    UserDataSource {
    override suspend fun changePassword(password: String): BasicState {
        val changePasswordResponse= ChangePasswordResponse(
            sessionStorage.getEmail(),
            password
        )
        val response = userServiceWithToken.changePassword(changePasswordResponse)
        return if (response.isSuccessful) {
            BasicState.SuccessState
        } else {
            BasicState.ErrorState
        }
    }
}