package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.*
import android.view.View

class PieView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.apply {
            val paint = Paint().apply {
                color = Color.parseColor("#c7a8c7")
                strokeWidth = 2F
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            val rectF = RectF(90F, 30F, 300F, 180F)
            val rectFOffset5 = RectF(95F, 25F, 305F, 180F)

            drawColor(Color.parseColor("#e7e7e7"))
            drawPie(rectF, this, 0F, 120F, paint)
            drawPie(rectF, this, 120F, 30F, paint.also { it.color = Color.parseColor("#d5c7a2") })
            drawPie(rectF, this, 150F, 110F, paint.also { it.color = Color.parseColor("#b3f5c3") })
            drawPie(rectF, this, 260F, 40F, paint.also { it.color = Color.parseColor("#a3bf7e") })
            drawPie(rectFOffset5, this, 300F, 60F, paint.also { it.color = Color.parseColor("#D81B60") })



            paint.shader =SweepGradient(150F,400F,Color.parseColor("#00574B"),Color.parseColor("#c9a3b6"))
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 10F
            drawCircle(150F,400F,120F,paint)
        }
    }

    private fun drawPie(rectF: RectF, canvas: Canvas, start: Float, sweep: Float, paint: Paint) {
        canvas.drawArc(rectF, start, sweep, true, paint)
    }

}
