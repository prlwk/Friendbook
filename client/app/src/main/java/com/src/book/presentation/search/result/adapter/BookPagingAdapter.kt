package com.src.book.presentation.search.result.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.src.book.R
import com.src.book.databinding.ViewHolderBookBinding
import com.src.book.domain.model.book.BookList
import com.src.book.presentation.utils.RatingColor

class BookPagingAdapter(
    private val onClickMore: ((it: BookList) -> Unit)?,
    private val onClickBook: (it: BookList) -> Unit,
    private val onCLickBookmark: ((it: BookList) -> Unit)?
) :
    PagingDataAdapter<BookList, BookPagingAdapter.DataViewHolder>(BookListCallBack()) {
    private lateinit var binding: ViewHolderBookBinding

    class DataViewHolder(private val binding: ViewHolderBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            book: BookList, onClickMore: ((it: BookList) -> Unit)?,
            onClickBook: (it: BookList) -> Unit,
            onCLickBookmark: ((it: BookList) -> Unit)?
        ) {
            //TODO добавить placeholder (картинка, которая будет, если не загружается сама картинка с ссылки)
            Glide.with(context)
                .load(book.linkCover)
                .into(binding.ivBook)
            binding.tvBookName.text = book.name
            binding.tvBookAuthor.text = book.authors?.joinToString(", ") { it.name }
            with(binding.tvGlobalRating) {
                text = book.rating.toString()
                setTextColor(ContextCompat.getColor(context, RatingColor.getColor(book.rating)))
            }
            if (book.genres != null && book.genres.isNotEmpty()) {
                binding.tvBookYearGenre.text =
                    "${book.year}, " + book.genres.joinToString(", ") { it.name }
            } else {
                binding.tvBookYearGenre.text =
                    book.year
            }
            if (onClickMore != null) {
                binding.ivMore.setOnClickListener {
                    onClickMore(book)
                }
            } else {
                binding.ivMore.visibility = View.GONE
            }
            itemView.setOnClickListener {
                onClickBook(book)
            }
            if (onCLickBookmark != null) {
                binding.ivBookmark.setOnClickListener {
                    onCLickBookmark(book)
                    if (book.isAuth) {
                        setColorForBookMark(book, context)
                    }
                }
            }
            setColorForBookMark(book, context)
            if (book.isAuth) {
                if (book.grade != null) {
                    binding.tvRating.text = book.grade.toString()
                    binding.userRating.background = ContextCompat.getDrawable(
                        context,
                        RatingColor.getBackground(book.grade.toDouble())
                    )
                } else {
                    binding.userRating.visibility = View.GONE
                }

            } else {
                binding.userRating.visibility = View.GONE
            }
        }

        private fun setColorForBookMark(book: BookList, context: Context) {
            if (book.isWantToRead) {
                binding.ivBookmark.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.basic_color
                    )
                )
            } else {
                binding.ivBookmark.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.icon_nav_not_selected
                    )
                )
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.onBind(item, onClickMore, onClickBook, onCLickBookmark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }
}

class BookListCallBack : DiffUtil.ItemCallback<BookList>() {
    override fun areItemsTheSame(oldItem: BookList, newItem: BookList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookList, newItem: BookList): Boolean {
        return oldItem == newItem
    }

}