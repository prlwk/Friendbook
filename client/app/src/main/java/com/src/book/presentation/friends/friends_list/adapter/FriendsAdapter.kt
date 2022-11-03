package com.src.book.presentation.friends.friends_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.src.book.databinding.ViewHolderFriendsItemBinding
import com.src.book.domain.model.Friend

//TODO добавить onClickListener
//TODO добавить placeholder
class FriendsAdapter :
    ListAdapter<Friend, FriendsAdapter.DataViewHolder>(FriendsListDiffCallback()) {
    private lateinit var binding: ViewHolderFriendsItemBinding

    class DataViewHolder(private val binding: ViewHolderFriendsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            friend: Friend
        ) {
            Glide.with(context)
                .load(friend.image)
                .into(binding.ivUserPhoto)
            binding.tvUserName.text = friend.name
            binding.tvLogin.text = "@${friend.login}"
            binding.tvReviewsNumber.text = friend.countReviews.toString()
            binding.tvBooksReadNumber.text = friend.countRateBooks.toString()
            binding.tvWantReadNumber.text = friend.countWantToReadBooks.toString()
        }

        private val RecyclerView.ViewHolder.context get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderFriendsItemBinding.inflate(
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

class FriendsListDiffCallback : DiffUtil.ItemCallback<Friend>() {
    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem == newItem
    }

}