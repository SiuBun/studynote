package com.wsb.customview.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * An ItemDecoration for adding spacing to a RecyclerView with a GridLayoutManager.
 * This decoration only adds spacing between items, not on the outer edges.
 *
 * @param spacing The spacing to be applied between items.
 */
class GridSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val spanCount = layoutManager.spanCount
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // Add top spacing only for items from the second row onwards
        if (position >= spanCount) {
            outRect.top = spacing
        }

        // Add spacing to the right of all items except the last one in a row
        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount
    }
}