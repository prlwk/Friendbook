package com.src.book.data.remote.model.friend.friend

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.Friend
import com.src.book.utils.BASE_URL
import com.src.book.utils.USER_SERVICE_BASE_URL

class FriendMapper : Mapper<Friend, FriendResponse> {
    override suspend fun mapFromResponseToModel(data: FriendResponse): Friend {
        return Friend(
            id = data.id,
            name = data.name,
            login = data.login,
            countRateBooks = data.countRateBooks,
            countReviews = data.countReviews,
            countWantToReadBooks = data.countWantToReadBooks,
            image = "$BASE_URL$USER_SERVICE_BASE_URL${data.image}",
        )
    }
}