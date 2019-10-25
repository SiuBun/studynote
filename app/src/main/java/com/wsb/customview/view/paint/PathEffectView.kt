package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.*
import android.view.View
import android.graphics.SumPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.DashPathEffect



class PathEffectView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {

            val function: Path.() -> Unit = {
                rLineTo(150F, -200F)
                rLineTo(60F, 100F)
                rLineTo(120F, -80F)
                rLineTo(200F, 110F)
                close()
            }

            val cornerPath = Path().apply {
                moveTo(100F,300F)
            }.apply(function)

            val paint = Paint().apply {
                pathEffect = CornerPathEffect(30F)
                style = Paint.Style.STROKE
                strokeWidth = 5F
            }
            drawPath(cornerPath, paint)


            val discretePath = Path().apply {
                moveTo(100F,500F)
            }.apply(function)
            drawPath(discretePath,paint.apply {
                pathEffect = DiscretePathEffect(20F,10F)
            })


            val dashPath = Path().apply {
                moveTo(100F,700F)
            }.apply(function)
            drawPath(dashPath,paint.apply {
                pathEffect = DashPathEffect(floatArrayOf(20F,10F,5F,10F),0F)
            })


            val pathDash = Path().apply {
                rLineTo(40F,0F)
                rLineTo(-20F,30F)
                close()
            }

            val pathDashPathTranslate = Path().apply {
                moveTo(100F,900F)
            }.apply(function)
            drawPath(pathDashPathTranslate,paint.apply {
                pathEffect = PathDashPathEffect(pathDash,50F,0F,PathDashPathEffect.Style.TRANSLATE)
            })


            val pathDashPathMorph = Path().apply {
                moveTo(100F,1200F)
            }.apply(function)
            drawPath(pathDashPathMorph,paint.apply {
                pathEffect = PathDashPathEffect(pathDash,50F,0F,PathDashPathEffect.Style.MORPH)
            })

            val pathDashPathRotate = Path().apply {
                moveTo(100F,1500F)
            }.apply(function)
            drawPath(pathDashPathRotate,paint.apply {
                pathEffect = PathDashPathEffect(pathDash,50F,0F,PathDashPathEffect.Style.ROTATE)
            })


            val dashEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
            val discreteEffect = DiscretePathEffect(20f, 5f)
            paint.pathEffect = SumPathEffect(dashEffect, discreteEffect)

            drawPath(Path().apply {
                moveTo(100F,1700F)
            }.apply(function),paint)

            paint.pathEffect = ComposePathEffect(dashEffect, discreteEffect)
            drawPath(Path().apply {
                moveTo(100F,2000F)
            }.apply(function),paint)
        }
    }
}
