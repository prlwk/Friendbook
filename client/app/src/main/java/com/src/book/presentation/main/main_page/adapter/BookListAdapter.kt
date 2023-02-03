package com.src.book.presentation.main.main_page.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.src.book.R
import com.src.book.databinding.ViewHolderSimpleBookAndAuthorBinding
import com.src.book.domain.model.book.BookAuthor
import com.src.book.presentation.utils.RatingColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class BookListAdapter(private val onClickBook: (item: BookAuthor) -> Unit) :
    ListAdapter<BookAuthor, BookListAdapter.DataViewHolder>(BookAuthorDiffCallback()) {
    private lateinit var binding: ViewHolderSimpleBookAndAuthorBinding

    class DataViewHolder(private val binding: ViewHolderSimpleBookAndAuthorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun onBind(bookAuthor: BookAuthor, onClickBook: (item: BookAuthor) -> Unit) {
            Glide.with(context)
                .load(bookAuthor.linkCover)
                .placeholder(context.getDrawable(R.drawable.empty_photo_book_author))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivPhoto)
            val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
            decimalFormatSymbols.decimalSeparator = '.'
            if (bookAuthor.rating == 0.0) {
                binding.tvGlobalRating.text = context.resources.getText(R.string.no_rating)
            } else {
                binding.tvGlobalRating.text =
                    DecimalFormat("#0.0", decimalFormatSymbols).format(bookAuthor.rating)
            }
            binding.llGlobalRating.background =
                ContextCompat.getDrawable(context, RatingColor.getBackground(bookAuthor.rating))
            itemView.setOnClickListener {
                onClickBook(bookAuthor)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderSimpleBookAndAuthorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_in)
        holder.onBind(item, onClickBook)
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