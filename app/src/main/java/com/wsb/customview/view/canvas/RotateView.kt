package com.wsb.customview.view.canvas

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.wsb.customview.MainActivity
import com.wsb.customview.R

class RotateView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat()
        val top = ((height - bitmap.height) / 2).toFloat()
        canvas?.run {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            save()

            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
            rotate(
                    40F,
                    (left + bitmap.width / 2).also { Log.d(MainActivity.TAG, "图片旋转x轴为$it 像素") },
                    (top + bitmap.height / 2).also { Log.d(MainActivity.TAG, "图片旋转y轴为$it 像素") }
            )



            drawBitmap(bitmap, left, top, paint)

            restore()
        }
    }
}