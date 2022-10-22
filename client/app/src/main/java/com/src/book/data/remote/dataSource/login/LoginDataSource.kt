package com.src.book.data.remote.dataSource.login

import com.src.book.domain.utils.LoginState

interface LoginDataSource {
    suspend fun signIn(data: Map<String, String>):LoginState
}