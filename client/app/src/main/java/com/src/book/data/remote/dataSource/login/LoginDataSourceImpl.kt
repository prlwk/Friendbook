package com.src.book.data.remote.dataSource.login

import com.src.book.data.remote.model.login.loginAnswer.LoginAnswerResponse
import com.src.book.data.remote.service.LoginService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.utils.ERROR_EMAIL
import com.src.book.data.remote.utils.ERROR_NICKNAME
import com.src.book.data.remote.utils.ERROR_PASSWORD
import com.src.book.data.remote.utils.ErrorMessage
import com.src.book.domain.utils.LoginState

class LoginDataSourceImpl(val loginService: LoginService, val sessionStorage: SessionStorage) :
    LoginDataSource {
    override suspend fun signIn(data: Map<String, String>): LoginState {
        val registrationAnswerResponse = loginService.signIn(data)
        if (registrationAnswerResponse.isSuccessful) {
            println("УРААА")
            if (registrationAnswerResponse.body() != null) {
                val body = registrationAnswerResponse.body()!!
                println("token ${body.accessToken}")
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
}