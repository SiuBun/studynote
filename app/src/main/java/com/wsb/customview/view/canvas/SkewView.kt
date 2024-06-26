package com.wsb.customview.view.canvas

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class SkewView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat()
        val top = ((height - bitmap.height) / 2).toFloat()

        canvas?.run {
            val paint = Paint()

            save()

            skew(0.3F,0.1F)
            drawBitmap(bitmap,left,top,paint)

            restore()
        }
    }
}