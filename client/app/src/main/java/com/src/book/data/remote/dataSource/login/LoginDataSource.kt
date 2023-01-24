package com.src.book.data.remote.dataSource.login

import com.src.book.domain.model.user.Login
import com.src.book.domain.utils.*
import java.io.File

interface LoginDataSource {
    suspend fun signIn(data: Login): LoginState
    suspend fun checkEmailExists(email: String): BasicState<Boolean>
    suspend fun checkRecoveryCode(code: String, email: String): CodeState
    suspend fun sendCodeForRecoveryPassword(email: String): CodeState
    suspend fun registration(data: String, file: File?): RegistrationState
    suspend fun checkRecoveryCodeForAccountConfirmations(code: String, email: String): CodeState
    suspend fun sendCodeForAccountConfirmations(): BasicState<Unit>
}