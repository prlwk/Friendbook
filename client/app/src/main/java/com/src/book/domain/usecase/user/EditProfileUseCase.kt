package com.src.book.domain.usecase.user

import android.net.Uri
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.EditProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

class EditProfileUseCase(private val userRepository: UserRepository) {
    suspend fun execute(login: String, name: String, uri: Uri?): EditProfileState =
        withContext(Dispatchers.IO) {
            val json = JSONObject(
                mapOf(
                    "login" to login,
                    "name" to name,
                )
            )
            if (uri == null) {
                return@withContext userRepository.editProfile(json.toString(), null)
            }
            val file = uri.path?.let { File(it) }
            return@withContext userRepository.editProfile(json.toString(), file)
        }
}