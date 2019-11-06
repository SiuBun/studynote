package com.wsb.customview.view.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.utils.DrawUtils
import kotlin.math.cos
import kotlin.math.sin

class PieView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    lateinit var rectF: RectF

    /**
     * 画笔
     * */
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = DrawUtils.dp2px(2F)
        style = Paint.Style.FILL
    }
    val angleArray = floatArrayOf(
        50F,
        80F,
        160F,
        70F
    )
    val colorArray = intArrayOf(
        Color.parseColor("#7700FF00"),
        Color.parseColor("#77FF0000"),
        Color.parseColor("#77CDABFE"),
        Color.parseColor("#77456411")
    )

    var angleCurrent: Float = 0F

    var selectIndex = 2
        set(value) {
            if (value >= 0 && value < angleArray.size) {
                field = value
                postInvalidate()
            }
        }

    /**
     * 饼图半径
     * */
    internal var radius: Float = DrawUtils.dp2px(120F)

    var offsetValue: Float = DrawUtils.dp2px(5F)
    set(value) {
        if (value>0){
            field = value
            postInvalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(
            width / 2 - radius,
            height / 2 - radius,
            width / 2 + radius,
            height / 2 + radius
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            for ((index, angle) in angleArray.withIndex()) {
                if (index == selectIndex) {
                    save()
//                    偏离角度
                    val offsetAngle = (angleCurrent + angle / 2).toDouble()
                    translate(
                        ((cos(Math.toRadians(offsetAngle)))*offsetValue).toFloat(),
                        ((sin(Math.toRadians(offsetAngle)))*offsetValue).toFloat()
                    )
                }
                drawArc(rectF, angleCurrent, angle, true, paint.apply { color = colorArray[index] })

                if (index == selectIndex) {
                    restore()
                }
                angleCurrent += angle
            }
        }
    }
}