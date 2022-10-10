package com.src.book.data.remote

interface Mapper<M, R> {
    suspend fun mapFromResponseToModel(data: R): M
}