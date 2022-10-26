package com.src.book.domain.usecase.login

import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.CodeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class SendCodeForRecoveryPasswordUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(email: String): CodeState = withContext(Dispatchers.IO) {
        return@withContext loginRepository.sendCodeForRecoveryPassword(email.lowercase(Locale.getDefault()))
    }
}