package com.src.book.presentation.main.main_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.src.book.databinding.ViewHolderFilterItemBinding
import com.src.book.databinding.ViewHolderTitleBinding
import com.src.book.domain.model.book.genre.GenreWithTitle

class AllGenreFilterAdapter(private val onClickGenre: (item: GenreWithTitle.Genre) -> Unit) :
    ListAdapter<GenreWithTitle, AllGenreFilterAdapter.DataViewHolder>(GenreWithTitleDiffCallback()) {
    private lateinit var binding: ViewBinding

    class DataViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            genreWithTitle: GenreWithTitle,
            onClickGenre: (item: GenreWithTitle.Genre) -> Unit
        ) {
            when (genreWithTitle) {
                is GenreWithTitle.Genre -> {
                    onBindGenre(genreWithTitle, onClickGenre)
                }
                is GenreWithTitle.Title -> {
                    onBindTitle(genreWithTitle)
                }
            }
        }

        private fun onBindGenre(
            genreWithTitle: GenreWithTitle.Genre,
            onClickGenre: (item: GenreWithTitle.Genre) -> Unit
        ) {
            val bindingTag = binding as ViewHolderFilterItemBinding
            bindingTag.tvName.text = genreWithTitle.genreWithCheck.genre.name
            if (genreWithTitle.genreWithCheck.isSelected) {
                bindingTag.ivIsSelected.visibility = View.VISIBLE
            } else {
                bindingTag.ivIsSelected.visibility = View.GONE
            }
            itemView.setOnClickListener {
                if (genreWithTitle.genreWithCheck.isSelected) {
                    bindingTag.ivIsSelected.visibility = View.GONE
                } else {
                    bindingTag.ivIsSelected.visibility = View.VISIBLE
                }
                onClickGenre(genreWithTitle)
            }
        }

        private fun onBindTitle(title: GenreWithTitle.Title) {
            val bindingTitle = binding as ViewHolderTitleBinding
            bindingTitle.tvTitle.text = title.title
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GenreWithTitle.Title -> TYPE_TITLE
            is GenreWithTitle.Genre -> TYPE_GENRE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        when (viewType) {
            TYPE_GENRE -> {
                binding = ViewHolderFilterItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
            TYPE_TITLE -> {
                binding = ViewHolderTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
        }
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickGenre)
    }

    private companion object {
        private const val TYPE_GENRE = 0
        private const val TYPE_TITLE = 1
    }
}

class GenreWithTitleDiffCallback : DiffUtil.ItemCallback<GenreWithTitle>() {
    override fun areItemsTheSame(oldItem: GenreWithTitle, newItem: GenreWithTitle): Boolean {
        if (oldItem is GenreWithTitle.Title && newItem is GenreWithTitle.Title) {
            return oldItem.title == newItem.title
        }
        if (oldItem is GenreWithTitle.Genre && newItem is GenreWithTitle.Genre) {
            return (oldItem.genreWithCheck.genre.id == newItem.genreWithCheck.genre.id)
        }
        return false
    }

    override fun areContentsTheSame(oldItem: GenreWithTitle, newItem: GenreWithTitle): Boolean {
        return oldItem == newItem
    }
}
