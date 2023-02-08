package com.src.book.data.local.repository

interface LocalUserRepository {
    suspend fun setIsActiveAndClearSession()

}