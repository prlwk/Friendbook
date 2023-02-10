package com.src.book.presentation.search.result.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.src.book.R
import com.src.book.databinding.ViewHolderSimpleResultAuthorBinding
import com.src.book.domain.model.author.AuthorList
import com.src.book.presentation.utils.RatingColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class SimpleAuthorPagingAdapter(private val onClickAuthor: (item: AuthorList) -> Unit) :
    PagingDataAdapter<AuthorList, SimpleAuthorPagingAdapter.DataViewHolder>(AuthorListCallback()) {

    class DataViewHolder(private val binding: ViewHolderSimpleResultAuthorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun onBind(authorList: AuthorList, onClickAuthor: (item: AuthorList) -> Unit) {
            Glide.with(context)
                .load(authorList.photoSrc)
                .placeholder(context.getDrawable(R.drawable.empty_photo_book_author))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivPhoto)
            binding.tvName.text = authorList.name
            val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
            decimalFormatSymbols.decimalSeparator = '.'
            if (authorList.rating == null || authorList.rating == 0.0) {
                binding.tvGlobalRating.text = context.resources.getText(R.string.no_rating)
            } else {
                binding.tvGlobalRating.text =
                    DecimalFormat("#0.0", decimalFormatSymbols).format(authorList.rating)
            }
            binding.llGlobalRating.background =
                ContextCompat.getDrawable(context, RatingColor.getBackground(authorList.rating))
            itemView.setOnClickListener {
                onClickAuthor(authorList)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.onBind(item, onClickAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ViewHolderSimpleResultAuthorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }
}