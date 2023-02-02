package com.src.book.presentation.book.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.ViewHolderCategoryBinding
import com.src.book.domain.model.Genre

//TODO сделать кликабельным
class GenreAdapter : ListAdapter<Genre, GenreAdapter.DataViewHolder>(GenreDiffCallBack()) {
    private lateinit var binding: ViewHolderCategoryBinding

    class DataViewHolder(binding: ViewHolderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tag = binding.tvName
        fun onBind(genre: Genre) {
            this.tag.text = genre.name
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GenreAdapter.DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}

class GenreDiffCallBack : DiffUtil.ItemCallback<Genre>() {
    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }

}