package com.wsb.customview.view.practice

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.DrawUtils
import kotlin.math.min

class CircleProgress @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var value: Float = 0F
        set(value) {
            if (value in 0.0..100.0) {
                field = value
                invalidate()
            }
        }

    private var bodyPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(10F)
        strokeCap = Paint.Cap.ROUND
    }

    private var valuePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textSize = DrawUtils.dp2px(40F)
        textAlign = Paint.Align.LEFT
        color = Color.parseColor("#F5DEB3")
    }

    private var centerX = 0F
    private var centerY = 0F
    private var radius = 0F


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()

        radius = min(centerX, centerY) * 0.8F

        canvas?.run {

            drawColor(Color.GRAY)

            drawCircle(centerX, centerY, radius, bodyPaint.apply {
                color = Color.parseColor("#e7e7e7")
            })

            save()

            rotate(-90F, centerX, centerY)

            drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, 0F, value / 100 * 360, false, bodyPaint.apply {
                color = Color.RED
            })

            restore()

            val string = "Rng"
            val bounds = Rect()
            valuePaint.getTextBounds(string, 0, string.length, bounds)

            val textYByRect = centerY - valuePaint.let {
                (bounds.bottom + bounds.top) / 2
            }

            val textYByFontMetrics = centerY - valuePaint.let {
                it.fontMetrics.run {
                    (descent+ascent)/2
                }
            }
            drawText(
                    string,
                    centerX - valuePaint.measureText(string) / 2,
                    textYByFontMetrics,
                    valuePaint)


            drawText("Msi",0F-bounds.left,0F-bounds.top,valuePaint)
        }
    }
}