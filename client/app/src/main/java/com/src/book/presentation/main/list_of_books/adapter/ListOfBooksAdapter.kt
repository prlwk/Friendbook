package com.src.book.presentation.main.list_of_books.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.src.book.databinding.ViewHolderBookBinding
import com.src.book.domain.model.Book
import com.src.book.presentation.utils.RatingColor

//TODO дописать сохранена ли книга пользователем + его оценка
//TODO Если нет рейтинга что делать
class ListOfBooksAdapter(
    private val onClickMore: (it: Book) -> Unit
) :
    ListAdapter<Book, ListOfBooksAdapter.DataViewHolder>(BookDiffCallback()) {
    private lateinit var binding: ViewHolderBookBinding

    class DataViewHolder(binding: ViewHolderBookBinding) : RecyclerView.ViewHolder(binding.root) {
        private val ivBook = binding.ivBook
        private val tvBookName = binding.tvBookName
        private val tvAuthor = binding.tvBookAuthor
        private val tvGenres = binding.tvBookYearGenre
        private val ivBookmark = binding.ivBookmark
        private val tvRating = binding.tvRating
        private val clRating = binding.userRating
        private val tvGlobalRating = binding.tvGlobalRating
        private val ivMore = binding.ivMore

        fun onBind(book: Book, onClickMore: (it: Book) -> Unit) {
            Glide.with(context)
                .load(book.linkCover)
                .into(ivBook)
            tvBookName.text = book.name
            tvAuthor.text = book.authors.joinToString(", ") { it.name }
            ivBookmark.visibility = View.GONE
            clRating.visibility = View.GONE
            with(tvGlobalRating) {
                text = book.rating.toString()
                setTextColor(ContextCompat.getColor(context, RatingColor.getColor(book.rating)))
            }
            tvGenres.text = book.genres.joinToString(", ") { it.name }
            ivMore.setOnClickListener {
                onClickMore(book)
            }

        }

        private val RecyclerView.ViewHolder.context get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickMore)
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}