package com.src.book.presentation.book.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.ViewHolderAuthorNameBinding
import com.src.book.domain.model.AuthorBook

//TODO кликабельное имя
class AuthorNameAdapter(
    private val onClickAuthor: (item: AuthorBook) -> Unit
) :
    ListAdapter<AuthorBook, AuthorNameAdapter.DataViewHolder>(AuthorDiffCallBack()) {
    private lateinit var binding: ViewHolderAuthorNameBinding

    class DataViewHolder(private val binding: ViewHolderAuthorNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(author: AuthorBook, onClickAuthor: (item: AuthorBook) -> Unit) {
            binding.tvAuthorName.text = author.name
            binding.root.setOnClickListener {
                onClickAuthor(author)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderAuthorNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AuthorNameAdapter.DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickAuthor)
    }
}

class AuthorDiffCallBack : DiffUtil.ItemCallback<AuthorBook>() {
    override fun areItemsTheSame(oldItem: AuthorBook, newItem: AuthorBook): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AuthorBook, newItem: AuthorBook): Boolean {
        return oldItem == newItem
    }

}