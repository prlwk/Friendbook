package com.src.book.data.remote.model.tag

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.Tag

class TagMapper : Mapper<Tag, TagResponse> {
    override suspend fun mapFromResponseToModel(data: TagResponse): Tag {
        return Tag(
            id = data.id,
            name = data.name
        )
    }
}