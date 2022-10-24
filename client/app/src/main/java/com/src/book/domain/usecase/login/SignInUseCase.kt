package com.src.book.domain.usecase.login

import com.src.book.domain.model.user.Login
import com.src.book.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(email: String, password: String, isEntryByEmail: Boolean) =
        withContext(Dispatchers.IO) {
            val data =
                Login(loginOrEmail = email, password = password, isEntryByEmail = isEntryByEmail)
            return@withContext loginRepository.signIn(data)
        }
}