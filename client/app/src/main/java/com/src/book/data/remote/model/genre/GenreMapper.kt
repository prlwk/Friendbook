package com.src.book.data.remote.model.genre

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.Genre

class GenreMapper : Mapper<Genre, GenreResponse> {
    override suspend fun mapFromResponseToModel(data: GenreResponse): Genre {
        return Genre(
            id = data.id,
            name = data.name
        )
    }
}