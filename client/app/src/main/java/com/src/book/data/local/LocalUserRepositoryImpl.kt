package com.src.book.data.local

import com.src.book.data.remote.session.SessionStorage

class LocalUserRepositoryImpl(private val sessionStorage: SessionStorage):LocalUserRepository {
    override suspend fun setIsActiveAndClearSession() {
        sessionStorage.clearSession()
        sessionStorage.setIsActive(true)
    }
}