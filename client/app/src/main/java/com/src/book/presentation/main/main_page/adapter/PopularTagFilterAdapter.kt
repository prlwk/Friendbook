package com.src.book.presentation.main.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.ViewHolderCategoryFilterBinding
import com.src.book.domain.model.book.tag.TagWithCheck

class TagFilterAdapter(private val onClickTag: (item: TagWithCheck) -> Unit) :
    ListAdapter<TagWithCheck, TagFilterAdapter.DataViewHolder>(TagFilterDiffCallback()) {
    private lateinit var binding: ViewHolderCategoryFilterBinding

    class DataViewHolder(private val binding: ViewHolderCategoryFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(tagWithCheck: TagWithCheck, onClickTag: (item: TagWithCheck) -> Unit) {
            binding.tvName.text = tagWithCheck.tag.name
            if (tagWithCheck.isSelected) {
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
                if (tagWithCheck.isSelected) {
                    binding.tvName.background =
                        ContextCompat.getDrawable(context, R.drawable.category_filter_background)
                } else {
                    binding.tvName.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.selected_category_filter_background
                    )
                }
                onClickTag(tagWithCheck)
            }
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderCategoryFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, onClickTag)
    }
}

class TagFilterDiffCallback : DiffUtil.ItemCallback<TagWithCheck>() {
    override fun areItemsTheSame(oldItem: TagWithCheck, newItem: TagWithCheck): Boolean {
        return oldItem.tag.id == newItem.tag.id
    }

    override fun areContentsTheSame(oldItem: TagWithCheck, newItem: TagWithCheck): Boolean {
        return oldItem == newItem
    }
}
