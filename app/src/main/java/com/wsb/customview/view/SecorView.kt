package com.wsb.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class SecorView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val paint = Paint().apply {
                color = Color.parseColor("#c7a8c7")
                strokeWidth = 2F
                style = Paint.Style.STROKE
            }
            drawArc(80F, 20F, 200F, 100F, 30F, 80F, false, paint)
        }
    }
}
