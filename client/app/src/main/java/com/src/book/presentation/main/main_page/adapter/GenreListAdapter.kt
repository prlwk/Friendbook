package com.src.book.presentation.main.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.ViewHolderCategoryBinding
import com.src.book.domain.model.Genre

class GenreListAdapter(private val onClickGenre: (item: Genre) -> Unit) :
    ListAdapter<Genre, GenreListAdapter.DataViewHolder>(GenreDiffCallback()) {
    private lateinit var binding: ViewHolderCategoryBinding

    class DataViewHolder(private val binding: ViewHolderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(genre: Genre, onClickGenre: (item: Genre) -> Unit) {
            binding.tvName.text = genre.name
            itemView.setOnClickListener {
                onClickGenre(genre)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickGenre)
    }
}

class GenreDiffCallback : DiffUtil.ItemCallback<Genre>() {
    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }

}