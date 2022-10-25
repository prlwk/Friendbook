package com.src.book.domain.usecase.login

import com.src.book.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class CheckEmailExistsUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(email: String) = withContext(Dispatchers.IO) {
        return@withContext loginRepository.checkEmailExists(email.lowercase(Locale.getDefault()))
    }
}