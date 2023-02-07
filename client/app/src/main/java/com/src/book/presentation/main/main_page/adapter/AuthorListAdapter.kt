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
import com.src.book.domain.model.author.AuthorList
import com.src.book.presentation.utils.RatingColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AuthorListAdapter(private val onClickAuthor: (item: AuthorList) -> Unit) :
    ListAdapter<AuthorList, AuthorListAdapter.DataViewHolder>(AuthorListCallBack()) {
    private lateinit var binding: ViewHolderSimpleBookAndAuthorBinding

    class DataViewHolder(private val binding: ViewHolderSimpleBookAndAuthorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun onBind(authorList: AuthorList, onClickAuthor: (item: AuthorList) -> Unit) {
            Glide.with(context)
                .load(authorList.photoSrc)
                .placeholder(context.getDrawable(R.drawable.empty_photo_book_author))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivPhoto)
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
        holder.onBind(item, onClickAuthor)
    }
}

class AuthorListCallBack : DiffUtil.ItemCallback<AuthorList>() {
    override fun areItemsTheSame(oldItem: AuthorList, newItem: AuthorList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AuthorList, newItem: AuthorList): Boolean {
        return oldItem == newItem
    }

}