package com.src.book.presentation.main.main_page.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TagGenreFilterItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position == 0 || position == 7) {
            outRect.top = MARGIN_TOP
            outRect.bottom = MARGIN_BOTTOM
        }
        if (position == state.itemCount - 1) {
            outRect.bottom = MARGIN_BOTTOM
        }
    }

    private companion object {
        private const val MARGIN_TOP = 16
        private const val MARGIN_BOTTOM = 4
    }
}