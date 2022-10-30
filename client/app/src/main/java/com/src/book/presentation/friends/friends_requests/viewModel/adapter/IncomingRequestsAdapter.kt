package com.src.book.presentation.friends.friends_requests.viewModel.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.src.book.databinding.ViewHolderIncomingRequestPersonBinding

import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.utils.BASE_URL
import com.src.book.utils.USER_SERVICE_BASE_URL

class IncomingRequestsAdapter(
    private val onClickAdd: (request: FriendRequest, position: Int) -> Unit,
    private val onClickReject: (request: FriendRequest, position: Int) -> Unit
) :
    ListAdapter<FriendRequest, IncomingRequestsAdapter.DataViewHolder>(DiffCallback()) {
    private lateinit var binding: ViewHolderIncomingRequestPersonBinding

    class DataViewHolder(private val binding:  ViewHolderIncomingRequestPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            friendRequest: FriendRequest,
            onClickAdd: (request: FriendRequest, position: Int) -> Unit,
            onClickReject: (request: FriendRequest, position: Int) -> Unit
        ) {
            Glide.with(context)
                .load("$BASE_URL$USER_SERVICE_BASE_URL/${friendRequest.imageUrl}")
                .into(binding.ivUserPhoto)
            binding.tvUserName.text = friendRequest.name
            binding.tvLogin.text = friendRequest.login
            binding.addButton.setOnClickListener {
                onClickAdd(friendRequest, layoutPosition)
            }
            binding.rejectButton.setOnClickListener {
                onClickReject(friendRequest, layoutPosition)
            }
        }

        private val RecyclerView.ViewHolder.context get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding =  ViewHolderIncomingRequestPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickAdd, onClickReject)

    }
}