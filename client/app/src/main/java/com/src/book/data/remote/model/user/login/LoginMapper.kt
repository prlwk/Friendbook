package com.src.book.data.remote.model.user.login

import com.src.book.domain.model.user.Login

class LoginMapper {
    suspend fun mapFromModelToResponse(login: Login) = LoginResponse(
        loginOrEmail = login.loginOrEmail,
        password = login.password,
        isEntryByEmail = login.isEntryByEmail.toString()
    )
}