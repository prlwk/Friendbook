package com.src.book.presentation.main.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.ViewHolderSearchItemBinding
import com.src.book.domain.model.search.SearchItem

class SearchItemAdapter(
    private val onClickDelete: (id: Long, position: Int) -> Unit,
    private val onClickSearchItem: (name: String) -> Unit
) :
    ListAdapter<SearchItem, SearchItemAdapter.DataViewHolder>(SearchItemDiffCallback()) {
    class DataViewHolder(private val binding: ViewHolderSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            searchItem: SearchItem,
            onClickDelete: (id: Long, position: Int) -> Unit,
            onClickSearchItem: (name: String) -> Unit
        ) {
            binding.tvName.text = searchItem.name
            binding.ivDelete.setOnClickListener {
                onClickDelete(searchItem.id, absoluteAdapterPosition)
            }
            itemView.setOnClickListener {
                onClickSearchItem(searchItem.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ViewHolderSearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickDelete = onClickDelete, onClickSearchItem = onClickSearchItem)
    }
}

class SearchItemDiffCallback : DiffUtil.ItemCallback<SearchItem>() {
    override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
        return oldItem == newItem
    }
}
