package com.src.book

import com.src.book.data.remote.model.author.author.AuthorResponse
import com.src.book.data.remote.model.author.authorBook.AuthorBookResponse
import com.src.book.data.remote.model.book.book.BookResponse
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorResponse
import com.src.book.data.remote.model.friend.request.FriendRequestResponse
import com.src.book.data.remote.model.genre.GenreResponse
import com.src.book.data.remote.model.login.emailExists.EmailExistsResponse
import com.src.book.data.remote.model.login.loginAnswer.LoginAnswerResponse
import com.src.book.data.remote.model.review.reviewBook.ReviewBookResponse
import com.src.book.data.remote.model.tag.TagResponse
import com.src.book.data.remote.model.login.login.LoginResponse

class TestModelsResponseGenerator {
    //author
    fun generateAuthorResponseModel() = AuthorResponse(
        id = ID,
        name = AUTHOR_NAME,
        yearsLife = AUTHOR_YEARS_LIFE,
        rating = RATING,
        photoSrc = null,
        biography = AUTHOR_BIOGRAPHY,
        books = listOf(generateBookAuthorResponseModel())
    )

    fun generateAuthorBookResponseModel() = AuthorBookResponse(
        id = ID,
        name = AUTHOR_NAME
    )

    //book
    fun generateBookResponseModel() = BookResponse(
        id = ID,
        name = BOOK_NAME,
        rating = RATING,
        linkCover = null,
        year = BOOK_YEAR,
        genres = listOf(generateGenreResponseModel()),
        authors = listOf(generateAuthorBookResponseModel()),
        reviews = listOf(generateReviewBookResponseModel()),
        description = BOOK_DESCRIPTION,
        tags = listOf(generateTagResponseModel())
    )

    fun generateBookAuthorResponseModel() = BookAuthorResponse(
        id = ID,
        rating = RATING,
        linkCover = null
    )

    //review
    fun generateReviewBookResponseModel() = ReviewBookResponse(
        id = ID,
        username = REVIEW_USER,
        photoSrc = null,
        reviewText = REVIEW_TEXT,
        rating = REVIEW_RATING
    )

    //tag
    fun generateTagResponseModel() = TagResponse(
        id = ID,
        name = TAG_NAME
    )

    //genre
    fun generateGenreResponseModel() = GenreResponse(
        id = ID,
        name = GENRE_NAME
    )

    //login
    fun generateLoginResponseModel() = LoginResponse(
        loginOrEmail = EMAIL,
        password = PASSWORD,
        isEntryByEmail = IS_ENTRY_BY_EMAIL.toString()
    )

    fun generateLoginAnswerResponse() = LoginAnswerResponse(
        accessToken = ACCESS_TOKEN,
        expireTimeAccessToken = EXPIRE_TIME_ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        expireTimeRefreshToken = EXPIRE_TIME_REFRESH_TOKEN,
        id = ID.toString(),
        email = EMAIL
    )

    fun generateEmailExistsTrueResponse() = EmailExistsResponse(
        exists = true
    )

    fun generateEmailExistsFalseResponse() = EmailExistsResponse(
        exists = false
    )
    fun generateFriendRequestResponse() = FriendRequestResponse(
        id = ID,
        imageUrl = IMAGE_URL,
        login = LOGIN,
        name = NAME
    )
}