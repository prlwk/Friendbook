package com.src.book

import com.src.book.domain.model.author.Author
import com.src.book.domain.model.author.AuthorBook
import com.src.book.domain.model.*
import com.src.book.domain.model.book.Book
import com.src.book.domain.model.book.BookAuthor
import com.src.book.domain.model.book.BookList
import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.domain.model.user.Login
import com.src.book.utils.BASE_URL
import com.src.book.utils.USER_SERVICE_BASE_URL

class TestModelsGenerator {
    //author
    fun generateAuthorModel() = Author(
        id = ID,
        name = AUTHOR_NAME,
        yearsLife = AUTHOR_YEARS_LIFE,
        rating = RATING,
        photoSrc = null,
        biography = AUTHOR_BIOGRAPHY,
        books = listOf(generateBookAuthorModel())
    )

    fun generateAuthorBookModel() = AuthorBook(
        id = ID,
        name = AUTHOR_NAME,
    )

    //book
    fun generateBookModel() = Book(
        id = ID,
        name = BOOK_NAME,
        rating = RATING,
        linkCover = null,
        year = BOOK_YEAR,
        genres = listOf(generateGenreModel()),
        authors = listOf(generateAuthorBookModel()),
        reviews = listOf(generateReviewModel()),
        description = BOOK_DESCRIPTION,
        tags = listOf(generateTagModel()),
        isWantToRead = false,
        grade = RATING.toInt()
    )

    fun generateBookListModel(isAuth: Boolean) = BookList(
        id = ID,
        name = BOOK_NAME,
        rating = RATING,
        linkCover = null,
        year = BOOK_YEAR,
        genres = listOf(generateGenreModel()),
        authors = listOf(generateAuthorBookModel()),
        isWantToRead = false,
        grade = RATING.toInt(),
        isAuth = isAuth
    )

    fun generateBookAuthorModel() = BookAuthor(
        id = ID,
        rating = RATING,
        linkCover = null
    )

    fun generateListOfBooksModel() = listOf(generateBookModel())

    fun generateListOfBookListModel() = listOf(generateBookListModel(true))

    //review
    fun generateReviewModel() = Review(
        id = ID,
        username = REVIEW_USER,
        photoSrc = null,
        reviewText = REVIEW_TEXT,
        rating = REVIEW_RATING
    )

    //tag
    fun generateTagModel() = Tag(
        id = ID,
        name = TAG_NAME
    )

    //genre
    fun generateGenreModel() = Genre(
        id = ID,
        name = GENRE_NAME
    )

    //login
    fun generateLoginModel() = Login(
        loginOrEmail = EMAIL,
        password = PASSWORD,
        isEntryByEmail = IS_ENTRY_BY_EMAIL
    )

    //friends
    fun generateFriendRequestModel() = FriendRequest(
        id = ID,
        imageUrl = IMAGE_URL,
        login = LOGIN,
        name = NAME
    )

    fun generateFriendModel() = Friend(
        id = ID,
        countWantToReadBooks = 0,
        countReviews = 0,
        countRateBooks = 0,
        image =  "$BASE_URL${USER_SERVICE_BASE_URL}url",
        login = LOGIN,
        name = NAME
    )
}