package com.src.book.presentation.book.main_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R

class TagAdapter() :
    ListAdapter<String, TagAdapter.DataViewHolder>(StringDiffCallback()) {

    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val tag = itemView.findViewById<TextView>(R.id.tv_tag)
        fun onBind(tag: String) {
            this.tag.text = tag
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_tag, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}