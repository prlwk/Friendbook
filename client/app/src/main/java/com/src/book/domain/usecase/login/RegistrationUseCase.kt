package com.src.book.domain.usecase.login

import android.net.Uri
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

class RegistrationUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(
        email: String,
        password: String,
        login: String,
        name: String,
        uri: Uri?
    ): RegistrationState = withContext(Dispatchers.IO) {
        val json = JSONObject(
            mapOf(
                "login" to login,
                "name" to name,
                "email" to email,
                "password" to password
            )
        )
        val data = json.toString()
        if (uri == null) {
            return@withContext loginRepository.registration(data, null)
        }
        val file = uri.path?.let { File(it) }
        return@withContext loginRepository.registration(data, file)
    }
}