package com.src.book.data.remote.dataSource.login

import com.src.book.data.remote.model.login.loginAnswer.LoginAnswerResponse
import com.src.book.data.remote.model.login.login.LoginMapper
import com.src.book.data.remote.service.LoginService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.utils.*
import com.src.book.domain.model.user.Login
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.LoginState

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

    override suspend fun checkEmailExists(email: String): Boolean {
        return loginService.checkEmailExists(email).body()?.exists!!
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
}