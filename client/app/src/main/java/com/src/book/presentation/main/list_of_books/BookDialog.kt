package com.src.book.presentation.main.list_of_books

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.src.book.R
import com.src.book.domain.model.Book

class BookDialog(context: Context, private val book: Book) : Dialog(context) {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_dialog)
        val ivBook = findViewById<ImageView>(R.id.iv_book)
        val tvBookName = findViewById<TextView>(R.id.tv_book_name)
        val tvBookAuthor = findViewById<TextView>(R.id.tv_book_author)
        val tvBookYear = findViewById<TextView>(R.id.tv_book_year)
        Glide.with(context)
            .load(book.linkCover)
            .into(ivBook)
        tvBookName.text = book.name
        tvBookAuthor.text = book.authors.joinToString(", ") { it.name }
        tvBookYear.text = book.year
    }
}