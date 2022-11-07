package com.src.book.data.remote.dataSource.user

import com.src.book.data.remote.model.token.RefreshTokenResponse
import com.src.book.data.remote.service.SessionService
import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.EditProfileState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserDataSourceImpl(
    private val userService: UserService,
    private val sessionService: SessionService,
    private val sessionStorage: SessionStorage
) :
    UserDataSource {
    override suspend fun changePassword(
        oldPassword: String?,
        newPassword: String
    ): ChangePasswordState {
        if (!sessionStorage.refreshTokenIsValid()) {
            val sessionResponse = sessionService.refreshTokens(
                RefreshTokenResponse(
                    generateRefreshToken = true,
                    email = sessionStorage.getEmail(),
                    accessToken = sessionStorage.getAccessToken(),
                    refreshToken = sessionStorage.getRefreshToken()
                )
            ).execute().body()
            sessionResponse?.let {
                sessionStorage.refreshAccessToken(
                    sessionResponse.accessToken,
                    it.expireTimeAccessToken
                )
                sessionResponse.refreshToken?.let { it1 ->
                    sessionResponse.expireTimeRefreshToken?.let { it2 ->
                        sessionStorage.refreshRefreshToken(
                            it1,
                            it2
                        )
                    }
                }
            }
        }
        val response = userService.changePassword(
            refreshToken = sessionStorage.getRefreshToken(),
            oldPassword = oldPassword,
            newPassword = newPassword
        )
        if (response.isSuccessful) {
            return ChangePasswordState.SuccessState
        }
        if (response.code() == 404) {
            return ChangePasswordState.WrongPasswordState
        }
        return ChangePasswordState.ErrorState
    }

    override suspend fun logout(): BasicState {
        val refreshToken = sessionStorage.getRefreshToken()
        val response = userService.logout(refreshToken)
        return if (response.isSuccessful) {
            sessionStorage.clearSession()
            BasicState.SuccessState
        } else {
            BasicState.ErrorState
        }
    }

    override suspend fun editProfile(data: String, file: File?): EditProfileState {
        val dataMultipart = data.toRequestBody("text/plain".toMediaTypeOrNull())
        var part: MultipartBody.Part? = null
        if (file != null) {
            val fileMultipart = file.asRequestBody("image/*".toMediaTypeOrNull())
            part = MultipartBody.Part.createFormData("file", file.name, fileMultipart)
        }
        val response = userService.editProfile(dataMultipart, part)
        if (response.isSuccessful) {
            return EditProfileState.SuccessState
        } else {
            if (response.code() == 409) {
                return EditProfileState.LoginAlreadyExistsState
            }
        }
        return EditProfileState.ErrorState
    }
}
