package com.src.book.domain.model.user

data class Login(
    val loginOrEmail: String,
    val password: String,
    val isEntryByEmail: Boolean
)