package com.src.book.presentation.author.list_of_books

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.src.book.R
import com.src.book.domain.model.book.BookList

//TODO вызвать окно "чтобы оценить, нужно зарегестрироваться, когда человек не авторизирован
//TODO повесить onCLickListener на оценить
class BookDialog(
    context: Context,
    private val book: BookList,
    private val onCLickBookmark: (it: BookList) -> Unit
) : Dialog(context) {
    private var bookGrateVisible =false
    private var bookBookmarkVisible =false

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_dialog)
        val ivBook = findViewById<ImageView>(R.id.iv_book)
        val tvBookName = findViewById<TextView>(R.id.tv_book_name)
        val tvBookAuthor = findViewById<TextView>(R.id.tv_book_author)
        val tvBookYear = findViewById<TextView>(R.id.tv_book_year)
        val clRating = findViewById<ConstraintLayout>(R.id.cl_rating)
        val clBookmark = findViewById<ConstraintLayout>(R.id.cl_bookmark)
        Glide.with(context)
            .load(book.linkCover)
            .into(ivBook)
        tvBookName.text = book.name
        if (book.isAuth) {
            bookGrateVisible = book.grade != null
            bookBookmarkVisible = book.isWantToRead
        }
        if (book.authors != null && book.authors.isNotEmpty()) {
            tvBookAuthor.text = book.authors.joinToString(", ") { it.name }
        } else {
            tvBookAuthor.visibility = View.GONE
        }
        tvBookYear.text = book.year
        setColorForBookmark(findViewById(R.id.iv_bookmark))
        clBookmark.setOnClickListener {
            bookBookmarkVisible = !bookBookmarkVisible
            if(book.isAuth) {
                setColorForBookmark(findViewById(R.id.iv_bookmark))
            }
            onCLickBookmark(book)
        }
    }


    private fun setColorForBookmark(ivBookmark: ImageView) {
        if (bookBookmarkVisible) {
            ivBookmark.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.basic_color
                )
            )
        } else {
            ivBookmark.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.color_accent
                )
            )
        }
    }
}