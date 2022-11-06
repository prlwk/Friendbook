package com.src.book.domain.usecase.login

import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.CodeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckRecoveryCodeForConfirmationsUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(email: String, code: String): CodeState = withContext(Dispatchers.IO) {
        return@withContext loginRepository.checkRecoveryCodeForAccountConfirmations(
            code = code,
            email = email
        )
    }
}