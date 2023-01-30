package com.src.book.presentation.main.main_page.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategoryItemDecoration :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position == 2 || position == 3) {
            outRect.left = MARGIN
            outRect.right = MARGIN
        }

        if (position % 2 == 0) {
            outRect.bottom = MARGIN
        }
    }

    private companion object {
        private const val MARGIN = 12
    }
}
