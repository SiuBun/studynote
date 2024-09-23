package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.utils.DrawUtils
import com.wsb.customview.R
import kotlin.math.cos
import kotlin.math.sin

class CoordinateExample @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerX: Float = 0F
    private var centerY: Float = 0F
    private var radius: Float = DrawUtils.dp2px(120F)

    /**
     * 维度对应的圆心角
     * */
    private var angle = Math.PI * 2 / 5

    private var paint: Paint = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.colorPrimary)
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(2F)
        textSize = DrawUtils.dp2px(14F)
    }

    private var tryPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.colorAccent)
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(2F)
        textSize = DrawUtils.dp2px(14F)
    }

    private var coordinatePaint: Paint = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(android.R.color.black)
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(1F)
        textSize = DrawUtils.dp2px(14F)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas?.run {
//            绘制基础坐标轴
            drawLine(0F,centerY,width.toFloat(),centerY,coordinatePaint)
            drawLine(width/2.toFloat(),0F,width/2.toFloat(),height.toFloat(),coordinatePaint)


            drawLine(centerX, centerY, getIndexX(radius, 0), getIndexY(radius, 0), paint)
            drawLine(centerX,centerY,getIndexX(radius,1),getIndexY(radius,1),paint)
//            drawLine(centerX,centerY,getIndexX(radius,2),getIndexY(radius,2),paint)
//            drawLine(centerX,centerY,getIndexX(radius,3),getIndexY(radius,3),paint)

            drawLine(centerX, centerY, xByRadian(0), yByRadian(0), tryPaint)
            drawLine(centerX, centerY, xByRadian(1), yByRadian(1), tryPaint)
//            drawLine(centerX, centerY, xByRadian(2), yByRadian(2), tryPaint)
//            drawLine(centerX, centerY, xByRadian(3), yByRadian(3), tryPaint)
        }
    }


    private fun xByRadian(i: Int): Float =
//    这种计算方式会以android坐标系的角度来计算
        centerX + radius * cos(Math.toRadians(360.0 / 5 * (i+1))).toFloat()

    private fun yByRadian(i: Int): Float =
//    这种计算方式会以android坐标系的角度来计算
        centerY + radius * sin(Math.toRadians(360.0 / 5 * (i+1))).toFloat()

    private fun getIndexY(curRadius: Float, index: Int, offset: Float = 0F): Float =
        centerY - (((curRadius + offset) * cos(angle * index))).toFloat()

    private fun getIndexX(curRadius: Float, index: Int, offset: Float = 0F): Float =
        centerX + (((curRadius + offset) * sin(angle * index))).toFloat()
}