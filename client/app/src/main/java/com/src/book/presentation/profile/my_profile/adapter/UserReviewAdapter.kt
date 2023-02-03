package com.src.book.presentation.profile.my_profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.ViewHolderProfileReviewBinding
import com.src.book.domain.model.userReview.UserReview
import com.src.book.presentation.utils.RatingColor

class UserReviewAdapter :
    ListAdapter<UserReview, UserReviewAdapter.DataViewHolder>(UserReviewDiffCallback()) {
    private lateinit var binding: ViewHolderProfileReviewBinding

    class DataViewHolder(private val binding: ViewHolderProfileReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(userReview: UserReview) {
            binding.tvAuthorName.text = userReview.authors?.joinToString(", ") { it.name }
            binding.tvReviewText.text = userReview.text
            binding.tvDate.text = userReview.date
            binding.tvRating.text = userReview.grade.toString()
            binding.tvBookName.text = userReview.bookName
            with(binding.llRating) {
              background  = ContextCompat.getDrawable(
                context,
                RatingColor.getBackground(userReview.grade.toDouble()))
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderProfileReviewBinding.inflate(
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
        holder.onBind(item)
    }
}

class UserReviewDiffCallback : DiffUtil.ItemCallback<UserReview>() {
    override fun areItemsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
        return oldItem == newItem
    }

}