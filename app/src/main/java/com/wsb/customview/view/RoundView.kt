package com.wsb.customview.view

import android.content.Context
import android.graphics.*
import android.view.View
import com.wsb.customview.R

class RoundView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val block: Paint.() -> Unit = {
                style = Paint.Style.STROKE
                color = Color.RED
                isAntiAlias = true
                strokeWidth = 10F
            }

            val paint = Paint().apply(block)

            drawCircle(400F,150F,100F,paint)

            paint.also {
                it.style = Paint.Style.FILL
                it.color = Color.BLUE
                it.alpha = 100
            }

            drawRect(50F, 500F, 130F, 650F,paint)


            paint.let{
                it.shader = LinearGradient(
                        50F,200F,300F,400F,
                        Color.parseColor("#E91E63"),
                        Color.parseColor("#2196F3"),
                        Shader.TileMode.CLAMP)
                it.alpha = 250
            }

            drawCircle(150F,300F,70F,paint)

            paint.shader = RadialGradient(280F, 500F, 100F, Color.parseColor("#E91E63"),
                    Color.parseColor("#2196F3"), Shader.TileMode.MIRROR)

            drawCircle(300F,500F,150F,paint)


            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.icon)
//            drawBitmap(bitmap,300F,250F,paint)

            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            drawCircle(120F,120F,100F,paint)
        }
    }
}
