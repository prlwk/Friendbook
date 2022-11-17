package com.src.book.data.remote.model.review.reviewUser

import com.src.book.data.remote.Mapper
import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import com.src.book.domain.model.userReview.UserReview

class UserReviewMapper(private val authorBookMapper: AuthorBookMapper) :
    Mapper<UserReview, UserReviewResponse> {
    override suspend fun mapFromResponseToModel(data: UserReviewResponse): UserReview {
        return UserReview(
            text = data.text,
            authors = data.authors?.map { authorBookMapper.mapFromResponseToModel(it) },
            bookId = data.bookId,
            bookName = data.bookName,
            date = data.date,
            grade = data.grade
        )
    }
}