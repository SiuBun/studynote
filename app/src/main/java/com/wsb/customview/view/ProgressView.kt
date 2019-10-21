package com.wsb.customview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.wsb.customview.DrawUtils
import com.wsb.customview.MainActivity
import java.util.concurrent.TimeUnit

class ProgressView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defInt: Int = 0) : View(context, attributeSet, defInt) {

    /**
     * 弧线旋转角度
     * */
    internal var rotateAngle: Float = 0F
    /**
     * 弧线最长角度
     * */
    internal var maxArcAngle: Float = 360F
        set(value) {
            if (value < 0) {
                Log.d(MainActivity.TAG, "偏移量不得小于0")
            } else {
                field = value
            }
        }

    internal var value: Int = 0
        set(value) {
            value.takeIf { value > 0 }?.also {
                field = it
            }
        }

    fun defineValue(v: Int) {
        ValueAnimator.ofInt(0, v).apply {
            duration = TimeUnit.SECONDS.toMillis(1)
            interpolator = LinearOutSlowInInterpolator()
            addUpdateListener {
                value = it.animatedValue as Int
                val progress = value.toFloat() / v.toFloat()
                alpha = progress
                scaleX = progress
                scaleY = progress
                invalidate()
            }
        }.start()
    }

    //    Object动画需要get set方法
    fun getValue(): Int = value

    fun setValue(v: Int) {
        value = v
        invalidate()
    }

    private var bodyPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dip2px(context, 20F)
        strokeCap = Paint.Cap.BUTT

    }
    private var valuePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textSize = DrawUtils.sp2px(context, 40F)
        textAlign = Paint.Align.LEFT
        color = Color.RED
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawColor(Color.parseColor("#e7e7e7"))

            val centerX = (width / 2).toFloat()
            val centerY = (height / 2).toFloat()
            val radius = (height / 2) * 0.8.toFloat()

            bodyPaint.apply {
                shader = SweepGradient(centerX, centerY, Color.parseColor("#D81B60"), Color.parseColor("#008577"))
            }


            val startAngle = rotateAngle
            val sweep = (maxArcAngle) / 100 * value.toFloat()
            drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, startAngle, if (value == 0) 0F else sweep, false, bodyPaint)

            val fontHeight = valuePaint.fontMetrics.run {
                descent - ascent
            }
            val text = "$value%"
            drawText(text, centerX - (valuePaint.measureText(text) / 2), centerY + fontHeight / 4, valuePaint)
        }
    }
}