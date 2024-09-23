package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class DitherFilterView : View {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attr: AttributeSet) : super(context, attr)

    constructor(context: Context?, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.run {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                isDither = true
                isFilterBitmap = true
            }

            drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.batman), 0F, 0F, paint)

        }
    }
}
