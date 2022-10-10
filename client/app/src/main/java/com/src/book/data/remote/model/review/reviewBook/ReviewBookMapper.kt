package com.src.book.data.remote.model.review.reviewBook

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.Review

class ReviewBookMapper : Mapper<Review, ReviewBookResponse> {
    override suspend fun mapFromResponseToModel(data: ReviewBookResponse): Review {
        return Review(
            id = data.id,
            username = data.username,
            photoSrc = data.photoSrc,
            reviewText = data.reviewText,
            rating = data.rating
        )
    }
}