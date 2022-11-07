package com.src.book.data.remote.dataSource.login

import com.src.book.data.remote.model.login.loginAnswer.LoginAnswerResponse
import com.src.book.data.remote.model.login.login.LoginMapper
import com.src.book.data.remote.service.LoginService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.utils.*
import com.src.book.domain.model.user.Login
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.LoginState
import com.src.book.domain.utils.RegistrationState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class LoginDataSourceImpl(
    private val loginService: LoginService,
    private val sessionStorage: SessionStorage,
    private val loginMapper: LoginMapper
) :
    LoginDataSource {
    override suspend fun signIn(data: Login): LoginState {
        val registrationAnswerResponse =
            loginService.signIn(loginMapper.mapFromModelToResponse(data))
        if (registrationAnswerResponse.isSuccessful) {
            if (registrationAnswerResponse.body() != null) {
                val body = registrationAnswerResponse.body()!!
                sessionStorage.refresh(
                    refreshToken = body.refreshToken,
                    expireTimeRefreshToken = body.expireTimeRefreshToken,
                    accessToken = body.accessToken,
                    expireTimeAccessToken = body.expireTimeAccessToken,
                    id = body.id,
                    email = body.email
                )
                sessionStorage.setIsActive(true)
            }
            return LoginState.SuccessState
        } else {
            if (registrationAnswerResponse.code() == 404) {
                val errorMessage = ErrorMessage<LoginAnswerResponse>()
                val message = errorMessage.getErrorMessage(registrationAnswerResponse)
                if (message == ERROR_PASSWORD) {
                    return LoginState.ErrorPasswordState
                } else if (message == ERROR_EMAIL || message == ERROR_NICKNAME) {
                    return LoginState.ErrorEmailLoginState
                }
            }
            return LoginState.ErrorServerState
        }
    }

    override suspend fun checkEmailExists(email: String): BasicState {
        val response = loginService.checkEmailExists(email)
        if (response.isSuccessful) {
            return BasicState
                .SuccessStateWithResources(response.body()?.exists)
        }
        return BasicState.ErrorState
    }

    override suspend fun checkRecoveryCode(code: String, email: String): CodeState {
        val response = loginService.checkRecoveryCode(code, email)
        if (response.isSuccessful) {
            if (response.body() != null) {
                val body = response.body()!!
                sessionStorage.refresh(
                    refreshToken = body.refreshToken,
                    expireTimeRefreshToken = body.expireTimeRefreshToken,
                    accessToken = body.accessToken,
                    expireTimeAccessToken = body.expireTimeAccessToken,
                    id = body.id,
                    email = body.email
                )
                sessionStorage.setIsActive(true)
            }
            return CodeState.SuccessState
        } else {
            if (response.code() == 409) {
                val errorMessage = ErrorMessage<LoginAnswerResponse>()
                val message = errorMessage.getErrorMessage(response)
                if (message == INVALID_CODE) {
                    return CodeState.WrongCodeState
                }
            }
        }
        return CodeState.ErrorState
    }

    override suspend fun sendCodeForRecoveryPassword(email: String): CodeState {
        val response = loginService.sendCodeForRecoveryPassword(email)
        if (response.isSuccessful) {
            return CodeState.SuccessState
        }
        if (response.code() == 404) {
            return CodeState.WrongEmailState
        }
        return CodeState.ErrorState
    }

    override suspend fun registration(data: String, file: File?): RegistrationState {
        val dataMultipart = data.toRequestBody("text/plain".toMediaTypeOrNull())
        var part: MultipartBody.Part? = null
        if (file != null) {
            val fileMultipart = file.asRequestBody("image/*".toMediaTypeOrNull())
            part = MultipartBody.Part.createFormData("file", file.name, fileMultipart)
        }

        val response = loginService.signUp(dataMultipart, part)

        if (response.isSuccessful) {
            val body = response.body()!!
            sessionStorage.refresh(
                refreshToken = body.refreshToken,
                expireTimeRefreshToken = body.expireTimeRefreshToken,
                accessToken = body.accessToken,
                expireTimeAccessToken = body.expireTimeAccessToken,
                id = body.id,
                email = body.email
            )
            return RegistrationState.SuccessState
        } else {
            if (response.code() == 409) {
                val errorMessage = ErrorMessage<LoginAnswerResponse>()
                val message = errorMessage.getErrorMessage(response)
                if (message == EMAIL_ALREADY_IN_USE) {
                    return RegistrationState.EmailAlreadyExistsState
                }
                return RegistrationState.LoginAlreadyExistsState
            }
        }
        return RegistrationState.ErrorState
    }

    override suspend fun checkRecoveryCodeForAccountConfirmations(
        code: String,
        email: String
    ): CodeState {
        val response = loginService.checkRecoveryCodeForAccountConfirmations(code, email)
        if (response.isSuccessful) {
            return CodeState.SuccessState
        }
        if (response.code() == 404) {
            return CodeState.WrongCodeState
        }
        return CodeState.ErrorState
    }

    override suspend fun sendCodeForAccountConfirmations(): BasicState {
        val id = sessionStorage.getId()
        if (id.isNotEmpty()) {
            sessionStorage.setIsActive(true)
            val response = loginService.sendCodeForAccountConfirmations(id.toLong())
            if (response.isSuccessful) {
                return BasicState.SuccessState
            }
        }
        return BasicState.ErrorState
    }
}