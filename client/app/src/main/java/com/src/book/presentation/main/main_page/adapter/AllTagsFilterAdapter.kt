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
import com.src.book.domain.model.book.tag.TagWithTitle

class AllTagFilterAdapter(private val onClickTag: (item: TagWithTitle.Tag) -> Unit) :
    ListAdapter<TagWithTitle, AllTagFilterAdapter.DataViewHolder>(TagWithTitleDiffCallback()) {
    private lateinit var binding: ViewBinding

    class DataViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            tagWithTitle: TagWithTitle,
            onClickTag: (item: TagWithTitle.Tag) -> Unit
        ) {
            when (tagWithTitle) {
                is TagWithTitle.Tag -> {
                    onBindTag(tagWithTitle, onClickTag)
                }
                is TagWithTitle.Title -> {
                    onBindTitle(tagWithTitle)
                }
            }
        }

        private fun onBindTag(
            tagWithTitle: TagWithTitle.Tag,
            onClickTag: (item: TagWithTitle.Tag) -> Unit
        ) {
            val bindingTag = binding as ViewHolderFilterItemBinding
            bindingTag.tvName.text = tagWithTitle.tagWithCheck.tag.name
            if (tagWithTitle.tagWithCheck.isSelected) {
                bindingTag.ivIsSelected.visibility = View.VISIBLE
            } else {
                bindingTag.ivIsSelected.visibility = View.GONE
            }
            itemView.setOnClickListener {
                if (tagWithTitle.tagWithCheck.isSelected) {
                    bindingTag.ivIsSelected.visibility = View.GONE
                } else {
                    bindingTag.ivIsSelected.visibility = View.VISIBLE
                }
                onClickTag(tagWithTitle)
            }
        }

        private fun onBindTitle(title: TagWithTitle.Title) {
            val bindingTitle = binding as ViewHolderTitleBinding
            bindingTitle.tvTitle.text = title.title
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TagWithTitle.Title -> TYPE_TITLE
            is TagWithTitle.Tag -> TYPE_TAG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        when (viewType) {
            TYPE_TAG -> {
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
        holder.onBind(item, onClickTag)
    }

    private companion object {
        private const val TYPE_TAG = 0
        private const val TYPE_TITLE = 1
    }
}

class TagWithTitleDiffCallback : DiffUtil.ItemCallback<TagWithTitle>() {
    override fun areItemsTheSame(oldItem: TagWithTitle, newItem: TagWithTitle): Boolean {
        if (oldItem is TagWithTitle.Title && newItem is TagWithTitle.Title) {
            return oldItem.title == newItem.title
        }
        if (oldItem is TagWithTitle.Tag && newItem is TagWithTitle.Tag) {
            return ((oldItem.tagWithCheck.tag.id == newItem.tagWithCheck.tag.id) && (oldItem.tagWithCheck.isSelected == newItem.tagWithCheck.isSelected))
        }
        return false
        //  return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TagWithTitle, newItem: TagWithTitle): Boolean {
        return oldItem == newItem
    }
}
