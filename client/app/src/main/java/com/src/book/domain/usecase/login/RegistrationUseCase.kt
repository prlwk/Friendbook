package com.src.book.domain.usecase.login

import android.net.Uri
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class RegistrationUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(data: String, uri: Uri?): RegistrationState = withContext(Dispatchers.IO) {
        if (uri == null) {
            return@withContext loginRepository.registration(data, null)
        }
        val file = uri.path?.let { File(it) }
        return@withContext loginRepository.registration(data, file)
    }
}