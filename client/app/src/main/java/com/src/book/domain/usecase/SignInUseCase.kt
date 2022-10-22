package com.src.book.domain.usecase

import com.src.book.domain.repository.LoginRepository
import com.src.book.utlis.IS_ENTRY_BY_EMAIL
import com.src.book.utlis.LOGIN_OR_EMAIL
import com.src.book.utlis.PASSWORD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(email: String, password: String, isEntryByEmail: Boolean) =
        withContext(Dispatchers.IO) {
            val data = mapOf(
                LOGIN_OR_EMAIL to email,
                PASSWORD to password,
                IS_ENTRY_BY_EMAIL to isEntryByEmail.toString()
            )
            return@withContext loginRepository.signIn(data)
        }
}