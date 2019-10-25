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

class ClipRectView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat().also { Log.d(MainActivity.TAG, "当前view宽度为$width ,图片宽度为${bitmap.width},绘图x轴上起始点为$it") }
        val top = ((height - bitmap.height) / 2).toFloat().also { Log.d(MainActivity.TAG, "当前view高度为$height ,图片高度为${bitmap.height},绘图y轴上起始点为$it") }
        canvas?.run {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            save()

            clipRect(left, top, left + 400F, top + 400F)
            drawBitmap(bitmap, left, top, paint)

            restore()
        }
    }

}