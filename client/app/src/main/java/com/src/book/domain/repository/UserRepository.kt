package com.src.book.domain.repository

import com.src.book.domain.utils.BasicState

interface UserRepository {
    suspend fun changePassword(password: String): BasicState
}