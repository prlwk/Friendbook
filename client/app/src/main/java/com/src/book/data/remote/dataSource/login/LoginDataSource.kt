package com.src.book.data.remote.dataSource.login

import com.src.book.domain.model.user.Login
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.LoginState

interface LoginDataSource {
    suspend fun signIn(data: Login): LoginState
    suspend fun checkEmailExists(email: String): Boolean
    suspend fun checkRecoveryCode(code: String, email: String): CodeState
    suspend fun sendCodeForRecoveryPassword(email: String): CodeState
}