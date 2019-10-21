package com.wsb.customview.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class ScaleView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr){

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat()
        val top = ((height - bitmap.height) / 2).toFloat()

        canvas?.run {
            val paint = Paint()
            save()

//            scaleX = 0.6F
            scale(0.3f, 0.8f, (top+bitmap.width / 2), (top+bitmap.height / 20))
            drawBitmap(bitmap,left,top,paint)

            restore()
        }
    }
}