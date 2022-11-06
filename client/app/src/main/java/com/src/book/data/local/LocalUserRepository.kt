package com.src.book.data.local

interface LocalUserRepository {
    suspend fun setIsActiveAndClearSession()

}