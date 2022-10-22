package com.src.book.domain.repository

import com.src.book.domain.model.user.Login
import com.src.book.domain.utils.LoginState

interface LoginRepository {
    suspend fun signIn(data: Login): LoginState
}