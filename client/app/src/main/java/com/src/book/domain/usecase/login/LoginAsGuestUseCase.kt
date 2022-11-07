package com.src.book.domain.usecase.login

import com.src.book.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginAsGuestUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute() = withContext(Dispatchers.IO){
        loginRepository.setIsActiveAndClearSession()
    }
}