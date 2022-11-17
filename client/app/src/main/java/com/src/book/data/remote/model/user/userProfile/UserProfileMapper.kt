package com.src.book.data.remote.model.user.userProfile

import com.src.book.data.remote.Mapper
import com.src.book.data.remote.model.book.bookList.BookListMapper
import com.src.book.data.remote.model.review.reviewUser.UserReviewMapper
import com.src.book.domain.model.user.UserProfile
import com.src.book.utils.BASE_URL
import com.src.book.utils.USER_SERVICE_BASE_URL

class UserProfileMapper(
    private val userReviewMapper: UserReviewMapper,
    private val bookListMapper: BookListMapper
) : Mapper<UserProfile, UserProfileResponse> {
    override suspend fun mapFromResponseToModel(data: UserProfileResponse): UserProfile {
        return UserProfile(
            email = data.email,
            login = data.login,
            name = data.name,
            ratedBooks = data.ratedBooks?.map { bookListMapper.mapFromResponseToModel(it, true) },
            reviews = data.reviews?.map { userReviewMapper.mapFromResponseToModel(it) },
            savingBooks = data.savingBooks?.map { bookListMapper.mapFromResponseToModel(it, true) },
            image = "$BASE_URL$USER_SERVICE_BASE_URL${data.image}",
            countFriends = data.countFriends
        )
    }
}