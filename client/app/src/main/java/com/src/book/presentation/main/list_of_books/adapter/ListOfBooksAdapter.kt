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
//TODO оценка пользователя
class ListOfBooksAdapter(
    private val onClickMore: (it: Book) -> Unit,
    private val onClickBook: (it: Book) -> Unit
) :
    ListAdapter<Book, ListOfBooksAdapter.DataViewHolder>(BookDiffCallback()) {
    private lateinit var binding: ViewHolderBookBinding

    class DataViewHolder(private val binding: ViewHolderBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            book: Book, onClickMore: (it: Book) -> Unit,
            onClickBook: (it: Book) -> Unit
        ) {
            Glide.with(context)
                .load(book.linkCover)
                .into(binding.ivBook)
            binding.tvBookName.text = book.name
            binding.tvBookAuthor.text = book.authors?.joinToString(", ") { it.name }
            binding.ivBookmark.visibility = View.GONE
            binding.userRating.visibility = View.GONE
            with(binding.tvGlobalRating) {
                text = book.rating.toString()
                setTextColor(ContextCompat.getColor(context, RatingColor.getColor(book.rating)))
            }
            binding.tvBookYearGenre.text =
                "${book.year}, " + book.genres?.joinToString(", ") { it.name }
            binding.ivMore.setOnClickListener {
                onClickMore(book)
            }
            itemView.setOnClickListener {
                onClickBook(book)
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
        holder.onBind(item, onClickMore, onClickBook)
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