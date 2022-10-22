package com.src.book.data.remote.dataSource.user

import com.src.book.domain.utils.BasicState

interface UserDataSource {
    suspend fun changePassword(password: String): BasicState
}