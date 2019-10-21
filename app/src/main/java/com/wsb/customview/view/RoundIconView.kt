package com.wsb.customview.view

import android.content.Context
import android.graphics.*
import android.view.View

class RoundIconView(context: Context?, private var icon: Int) : View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val paint = Paint()

            val bitmap = BitmapFactory.decodeResource(context.resources, icon)
//            drawBitmap(bitmap,300F,250F,paint)

            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            drawCircle(150F,150F,100F,paint)

        }
    }
}
