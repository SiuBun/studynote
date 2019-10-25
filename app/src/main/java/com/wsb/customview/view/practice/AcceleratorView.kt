package com.wsb.customview.view.practice

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.DrawUtils
import android.animation.Keyframe
import android.support.v4.view.animation.FastOutSlowInInterpolator
import java.util.concurrent.TimeUnit


class AcceleratorView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleInt: Int = 0) : View(context, attributeSet, defStyleInt) {

    internal var offsetAngle: Float = 30F

    private var rotateAngle: Float = 90F

    internal var value: Int = 0

    //    Object动画需要get set方法
    fun getValue(): Int = value

    fun setValue(v: Int) {
        value = v
        invalidate()
    }

    fun defineValue(v: Int) {
        // 在 0% 处开始
        val keyframe1 = Keyframe.ofInt(0f, 0)
        // 时间经过 50% 的时候，动画完成度 100%
        val keyframe2 = Keyframe.ofInt(0.5f, 100)
        // 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
        val keyframe3 = Keyframe.ofInt(1f, v)

        val keyframePropertyHolder = PropertyValuesHolder.ofKeyframe(
                "value",
                keyframe1,
                keyframe2,
                keyframe3
        )

        ObjectAnimator.ofPropertyValuesHolder(
                this,
                PropertyValuesHolder.ofFloat("scaleX", 0F,1F),
                PropertyValuesHolder.ofFloat("scaleY", 0F,1F),
                PropertyValuesHolder.ofFloat("alpha", 0F,1F),
                keyframePropertyHolder
        ).run {
            interpolator = FastOutSlowInInterpolator()
            duration = TimeUnit.SECONDS.toMillis(1)
            start()
        }

    }

    private var bodyPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dip2px(context, 20F)
        strokeCap = Paint.Cap.ROUND

    }
    private var valuePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        textSize = DrawUtils.sp2px(context, 40F)
        textAlign = Paint.Align.CENTER
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
                shader = SweepGradient(centerX, centerY, Color.RED, Color.GREEN)
            }

            save()

            val sweep = (360 - offsetAngle * 2) / 100 * value.toFloat()
            val startAngle = offsetAngle
            rotate(rotateAngle, centerX, centerY)
            drawArc(
                    centerX - radius,
                    centerY - radius,
                    centerX + radius,
                    centerY + radius,
                    startAngle,
                    if (value == 0) 0F else sweep,
                    false, bodyPaint)

            restore()

            val text = "$value%"
            drawText(text, centerX, centerY + ((valuePaint.descent() - valuePaint.ascent()) / 4), valuePaint)
        }
    }

}