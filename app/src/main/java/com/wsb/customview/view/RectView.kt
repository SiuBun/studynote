package com.wsb.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class RectView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.BLUE
        paint.alpha = 100
        canvas?.apply {
            drawColor(Color.parseColor("#e7dce7"))
            drawRect(100F, 150F, 200F, 300F,paint)
        }
    }
}
