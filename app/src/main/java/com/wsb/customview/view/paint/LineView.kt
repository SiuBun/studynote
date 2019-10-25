package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.*
import android.view.View

class LineView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.also {
            val paint = Paint().apply {
                strokeWidth = 10F
                color = Color.BLUE
                textSize = 20F
            }

            val floatArrayOf = floatArrayOf(
                    20F, 20F,
                    120F, 20F,
                    70F, 20F,
                    70F, 120F,
                    20F, 120F,
                    120F, 120F
            )
            it.drawLines(floatArrayOf, paint)

            it.drawText("卢本伟牛逼", 130F, 130F, paint)


            paint.shader = SweepGradient(
                    270F, 225F,
                    Color.parseColor("#E91E63"),
                    Color.parseColor("#2196F3"))

            paint.style = Paint.Style.STROKE
            paint.isAntiAlias = true
            paint.strokeWidth = 20F
            paint.strokeCap = Paint.Cap.ROUND

            val rectF = RectF(200F, 150F, 350F, 280F)
            it.drawArc(rectF,30F,280F,false,paint)

            val paint4Stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeWidth = 30F
                strokeJoin = Paint.Join.BEVEL
                style = Paint.Style.STROKE
            }


            val path = Path().apply {
                moveTo(50F,150F)
                lineTo(60F,300F)
                rLineTo(40F,60F)
                arcTo(150F,300F,460F,450F,-60F,180F,true)
//                close()
            }
            it.drawPath(path,paint4Stroke)
        }
    }
}
