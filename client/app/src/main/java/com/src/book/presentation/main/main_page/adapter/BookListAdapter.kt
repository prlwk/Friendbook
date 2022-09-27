package com.src.book.presentation.main.main_page.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.src.book.R
import com.src.book.databinding.ViewHolderSimpleBookBinding
import com.src.book.domain.model.BookAuthor
import com.src.book.presentation.utils.RatingColor


class BookListAdapter :
    ListAdapter<BookAuthor, BookListAdapter.DataViewHolder>(BookAuthorDiffCallback()) {
    private lateinit var binding: ViewHolderSimpleBookBinding

    class DataViewHolder(binding: ViewHolderSimpleBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ivBook = binding.ivBook
        private val tvGlobalRating = binding.tvGlobalRating
        private val llGlobalRating = binding.llGlobalRating

        fun onBind(bookAuthor: BookAuthor) {
            //TODO добавить placeholder (картинка, которая будет, если не загружается сама картинка с ссылки)
            Glide.with(context)
                .load(bookAuthor.linkCover)
                .into(ivBook)
            if (bookAuthor.rating == 0.0) {
                tvGlobalRating.text = context.resources.getText(R.string.no_rating)
            } else {
                tvGlobalRating.text = bookAuthor.rating.toString()
            }
            llGlobalRating.background =
                ContextCompat.getDrawable(context, RatingColor.getBackground(bookAuthor.rating))
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderSimpleBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}

class BookAuthorDiffCallback : DiffUtil.ItemCallback<BookAuthor>() {
    override fun areItemsTheSame(oldItem: BookAuthor, newItem: BookAuthor): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BookAuthor, newItem: BookAuthor): Boolean {
        return oldItem == newItem
    }
}