package com.src.book.presentation.book.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.ViewHolderExtendedReviewBinding
import com.src.book.domain.model.ExtendedReview

class ReviewListAdapter :
    ListAdapter<ExtendedReview, ReviewListAdapter.DataViewHolder>(ExtendedReviewDiffCallback()) {
    private lateinit var binding: ViewHolderExtendedReviewBinding

    class DataViewHolder(val binding: ViewHolderExtendedReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(extendedReview: ExtendedReview) {
            //TODO добавить placeholder (картинка, которая будет, если не загружается сама картинка с ссылки)
            /*Glide.with(context)
                .load(bookAuthor.linkCover)
                .into(ivBook)*/
            /*if (bookAuthor.rating == 0.0) {
                tvGlobalRating.text = context.resources.getText(R.string.no_rating)
            } else {
                tvGlobalRating.text = bookAuthor.rating.toString()
            }*/
            with(binding) {
                tvUserName.text = extendedReview.username
                tvReviewText.text = extendedReview.reviewText
                tvDate.text = extendedReview.reviewDate
                if (extendedReview.rating > 0) {
                    tvRating.text = extendedReview.rating.toString()
                } else {
                    tvRating.text = context.resources.getText(R.string.no_rating)
                    llRating.background =
                        ContextCompat.getDrawable(context, R.drawable.neural_rating_background)
                }

                when (extendedReview.rating) {
                    in 1..3 -> llRating.background =
                        ContextCompat.getDrawable(context, R.drawable.negative_rating_background)
                    in 4..6 -> llRating.background =
                        ContextCompat.getDrawable(context, R.drawable.neural_rating_background)
                    in 7..10 -> llRating.background =
                        ContextCompat.getDrawable(context, R.drawable.positive_rating_background)
                }
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    class ExtendedReviewDiffCallback : DiffUtil.ItemCallback<ExtendedReview>() {
        override fun areItemsTheSame(oldItem: ExtendedReview, newItem: ExtendedReview): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: ExtendedReview, newItem: ExtendedReview): Boolean {
            return oldItem == newItem;
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderExtendedReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewListAdapter.DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }

}