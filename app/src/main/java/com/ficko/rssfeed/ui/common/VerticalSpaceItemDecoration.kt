package com.ficko.rssfeed.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val distance: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (!isLastItem(view, parent)) {
            outRect.bottom = distance
        }
    }

    private fun isLastItem(view: View, parent: RecyclerView): Boolean =
        parent.adapter?.itemCount?.let { totalItemCount ->
            parent.getChildAdapterPosition(view) == (totalItemCount - 1)
        } ?: false
}