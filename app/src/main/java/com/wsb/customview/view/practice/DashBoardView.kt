package com.wsb.customview.view.practice

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.DrawUtils
import com.wsb.customview.R
import kotlin.math.cos
import kotlin.math.sin

class DashBoardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    /**
     * 仪表盘半径
     * */
    internal var radius: Float = DrawUtils.dp2px(150F)

    /**
     * 刻度段
     * */
    private var scaleCount: Float = 20F

    /**
     * 指针长度
     * */
    internal var scaleLength: Float = DrawUtils.dp2px(90F)
    /**
     * 弧线跨越的角度
     * */
    private var sweepAngle: Float = 300F
        set(value) {
            if (value >= 0 && value < 360) {
                field = value
                postInvalidate()
            }
        }

    /**
     * 对应弧线跨越的起始角度
     * */
    private val startAngle = (360 - sweepAngle) / 2

    /**
     * 外围弧线
     * */
    private var arcPath: Path = Path()


    /**
     * 刻度
     * */
    private var dash: Path = Path().apply {
        addRect(0F, 0F, DrawUtils.dp2px(2F), DrawUtils.dp2px(10F), Path.Direction.CCW)
    }

    /**
     * 弧线画笔
     * */
    private var arcPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.colorPrimaryDark)
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(4F)
    }

    /**
     * 刻度画笔
     * */
    private var scalePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.colorPrimary)
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(2F)
    }

    /**
     * 指针画笔
     * */
    private var valuePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.colorAccent)
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(2F)
    }

    private lateinit var mPathEffect: PathDashPathEffect

    /**
     * 当前刻度大小
     * */
    var value: Float = 0F
        set(value) {
            if (value >= 0 || value < scaleCount) {
                field = value
                postInvalidate()
            }
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        arcPath.apply {
            reset()
            addArc(getRectFarc(), startAngle, sweepAngle)
        }

        mPathEffect =
            PathDashPathEffect(
                dash,
//                此处切记不能用跨度除以数量 PathDashPathEffect(dash, sweepAngle / scaleCount, 0F, PathDashPathEffect.Style.ROTATE)
                (PathMeasure(arcPath, false).length - DrawUtils.dp2px(2F)) / scaleCount,
                0F,
                PathDashPathEffect.Style.ROTATE
            )


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            save()
            rotate(90F, (width / 2).toFloat(), (height / 2).toFloat())
//            drawArc(getRectFarc(),(360 - sweepAngle) / 2, sweepAngle,false,arcPaint)
            drawPath(arcPath, arcPaint)
            drawPath(arcPath, scalePaint.apply { pathEffect = mPathEffect })

            drawLine(
                (width / 2).toFloat(), (height / 2).toFloat(),
//                角度转弧度
                (width / 2 + (cos(Math.toRadians(getAngleByValue(value))) * scaleLength)).toFloat(),
//                (width / 2 + (cos(getAngleByValue(value)) * scaleLength)).toFloat(),
                (height / 2 + (sin(Math.toRadians(getAngleByValue(value))) * scaleLength)).toFloat(),
//                (height / 2 - (sin(getAngleByValue(value)) * scaleLength)).toFloat(),
                valuePaint
            )

            restore()
        }
    }

    private fun getAngleByValue(value: Float) =
        (value / scaleCount) * sweepAngle + startAngle.toDouble()

    private fun getRectFarc(): RectF =
        RectF(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius)
}