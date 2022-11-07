package com.src.book.domain.usecase.login

import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendCodeForConfirmationsUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(): BasicState = withContext(Dispatchers.IO) {
        return@withContext loginRepository.sendCodeForAccountConfirmations()
    }
}