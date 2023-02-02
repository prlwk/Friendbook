package com.src.book.presentation.main.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.ViewHolderCategoryFilterBinding
import com.src.book.domain.model.book.genre.GenreWithCheck

class GenreFilterAdapter(private val onClickGenre: (item: GenreWithCheck) -> Unit) :
    ListAdapter<GenreWithCheck, GenreFilterAdapter.DataViewHolder>(GenreFilterDiffCallback()) {
    class DataViewHolder(private val binding: ViewHolderCategoryFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(genreWithCheck: GenreWithCheck, onClickGenre: (item: GenreWithCheck) -> Unit) {
            binding.tvName.text = genreWithCheck.genre.name
            if (genreWithCheck.isSelected) {
                binding.tvName.background =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.selected_category_filter_background
                    )
            } else {
                binding.tvName.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.category_filter_background
                )
            }
            itemView.setOnClickListener {
                if (genreWithCheck.isSelected) {
                    binding.tvName.background =
                        ContextCompat.getDrawable(context, R.drawable.category_filter_background)
                } else {
                    binding.tvName.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.selected_category_filter_background
                    )
                }
                onClickGenre(genreWithCheck)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ViewHolderCategoryFilterBinding.inflate(
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

class GenreFilterDiffCallback : DiffUtil.ItemCallback<GenreWithCheck>() {
    override fun areItemsTheSame(oldItem: GenreWithCheck, newItem: GenreWithCheck): Boolean {
        return oldItem.genre.id == newItem.genre.id
    }

    override fun areContentsTheSame(oldItem: GenreWithCheck, newItem: GenreWithCheck): Boolean {
        return oldItem == newItem
    }

}