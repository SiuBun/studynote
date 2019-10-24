package com.wsb.customview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.wsb.customview.DrawUtils
import com.wsb.customview.MainActivity
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class RadarView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {
    /**
     * 雷达区颜色
     * */
    internal var mainColor = Color.parseColor("#008577")
    set(value) {
        field = value
        mainPaint.color = field
        invalidate()
    }

    /**
     * 分割线颜色
     * */
    internal var lineColor = Color.GREEN
        set(value) {
            field = value
            linePaint.color = field
            invalidate()
        }

    /**
     * 数据区颜色
     * */
    private val valueColor = Color.RED

    /**
     * 文本颜色
     * */
    private val textColor = -0x7f7f80
    private lateinit var mainPaint: Paint
    private lateinit var valuePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var linePaint: Paint
    /**
     * 绘制半径
     * */
    private var radius: Float = 0.0f
    /**
     * 控件宽度
     * */
    private var viewWidth = 0
    /**
     * 控件高度
     * */
    private var viewHeight = 0
    private var centerX = 0.0F
    private var centerY = 0.0F


    /**
     * 旋转角度
     * */
    private var rotateAngle = 0.0


    internal var data: Map<String,Float> = mutableMapOf("经济" to 0.8F,"伤害" to 0.6F,"走位" to 0.78F).also {
        it["助攻"] = 0.5F
        it["团战"] = 0.8F
        it["承受伤害"] = 0.2F
    }

        set(value) {
            value.takeUnless { it.isNullOrEmpty() }?.run {
                field = this
                keyList = field.keys.toMutableList()
                valueList = field.values.toMutableList()
                dimensionCount = field.size
                angle = Math.PI * 2 / dimensionCount
                invalidate()
            }
        }
    internal var keyList:List<String> = data.keys.toMutableList()
    internal var valueList:List<Float> = data.values.toMutableList()
    /**
     * 维度
     * */
    private var dimensionCount = keyList.size

    /**
     * 维度对应的圆心角
     * */
    private var angle = Math.PI * 2 / dimensionCount


    init {
        setupPaint()

//        sin(Math.PI / 18 * 14).also { Log.d(MainActivity.TAG, "sin140为$it") }
//        cos(Math.PI / 18 * 5).also { Log.d(MainActivity.TAG, "cos50为$it") }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (validData()) {
            canvas?.run {
                save()

                drawColor(Color.parseColor("#e7e7e7"))

                drawCircle()

                drawCrossLine()

                drawValue()

                drawDesc()

                restore()
            }
        } else {
            Log.d(MainActivity.TAG, "数据不合法,至少要三个数据")
        }
    }

    /**
     * 绘制文字说明
     * */
    private fun Canvas.drawDesc() {
        Path().apply {
            val textHeight = textPaint.fontMetrics.let {
                it.descent - it.ascent.also {
//                    h -> Log.d(MainActivity.TAG, "文字高度为${abs(h)}")
                }
            }

            for ((index,value)  in keyList.withIndex()) {
                val indexX = getIndexX(radius, index, textHeight)
                val indexY = getIndexY(radius, index, textHeight)
//                drawPoint(indexX,indexY,valuePaint)
                drawText(value, indexX - textPaint.measureText(value) / 2, indexY, textPaint)
            }
        }
    }

    /**
     * 绘制数值
     *
     * 从维度下标取下标对应数据,如果提供的数据不够维度多,则当前维度和后续数值为0
     * */
    private fun Canvas.drawValue() {

        drawPath(
                Path().apply {
                    for (index in valueList.indices) {
                        if (index < data.size) {
                            val curRadius = radius * valueList[index]
                            val indexX = getIndexX(curRadius, index)
                            val indexY = getIndexY(curRadius, index)
                            if (index == 0) {
                                moveTo(indexX, indexY)
                            } else {
                                lineTo(indexX, indexY)
                            }
                        } else {
                            lineTo(centerX,centerY)
                        }
                    }
                    close()
                }, valuePaint
        )
    }

    private fun validData() = data.size > 2

    /**
     * 绘制贯穿线
     *
     * 选择半径长度画穿线
     * */
    private fun Canvas.drawCrossLine() {
        if (lineColor != Color.TRANSPARENT) {
            drawPath(Path().apply {
                drawRadarCrossLine(radius)
            }, linePaint)
        }
    }

    /**
     * 绘制贯穿线
     *
     * @param curRadius 贯穿线长度
     * */
    private fun Path.drawRadarCrossLine(curRadius: Float) {
        for (j in 0 until dimensionCount) {
            moveTo(centerX, centerY)
            lineTo(getIndexX(curRadius, j), getIndexY(curRadius, j))
        }
    }

    /**
     *
     * 绘制圈数
     *
     * 计划比维度少1圈
     * */
    private fun Canvas.drawCircle() {
        for (layer in 1 until dimensionCount) {
            drawPath(
                    Path().apply {
                        drawRadarCircle(layer)
                    },
                    mainPaint.apply {
                        alpha = 50 / dimensionCount * layer
                    }
            )
        }

    }

    /**
     * 绘制雷达圈
     *
     * @param layer 雷达层数
     * */
    private fun Path.drawRadarCircle(layer: Int = 1) {
//        根据圈数确定绘制雷达圈厚度
        val curRadius = radius / (dimensionCount - 1) * layer

//            画圈;从连线到第1个点开始画,如果不使用close方法,就要绘制一条回原点的路径.就是当j == count时候
        for (j in 0 until dimensionCount) {
            if (j == 0) {
                moveTo(getIndexX(curRadius, j), getIndexY(curRadius, j))
            } else {
                lineTo(getIndexX(curRadius, j), getIndexY(curRadius, j))
            }
        }
        close()


    }

    private fun getIndexY(curRadius: Float, index: Int, offset: Float = 0F): Float =
            centerY - (((curRadius + offset) * cos(rotateAngle + angle * index))).toFloat()

    private fun getIndexX(curRadius: Float, index: Int, offset: Float = 0F): Float =
            centerX + (((curRadius + offset) * sin(rotateAngle + angle * index))).toFloat()

    private fun setupPaint() {
        mainPaint = Paint().apply {
            isAntiAlias = true
            color = mainColor
            style = Paint.Style.FILL
            strokeWidth = DrawUtils.dip2px(context, 1F)
//        strokeJoin = Paint.Join.ROUND
        }

        valuePaint = Paint().apply {
            isAntiAlias = true
            color = valueColor
            style = Paint.Style.STROKE
//            alpha = 50
            strokeWidth = DrawUtils.dip2px(context, 2F)
        }

        textPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = textColor
            textSize = DrawUtils.sp2px(context, 14F)
        }

        linePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = DrawUtils.dip2px(context, .5F)
            color = lineColor
        }
    }

    /**
     * 确定当前控件宽度高度,以此确定绘制的圆心
     * */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = DrawUtils.dip2px(context,80F)
        viewWidth = w
        viewHeight = h
//        Log.d(MainActivity.TAG, "控件宽度$viewWidth")
//        Log.d(MainActivity.TAG, "控件高度$viewHeight")

        centerX = (viewWidth / 2).toFloat()
//        Log.d(MainActivity.TAG, "控件圆心在x轴上$centerX")
        centerY = (viewHeight / 2).toFloat()
//        Log.d(MainActivity.TAG, "控件圆心在y轴上$centerY")

        postInvalidate()
        super.onSizeChanged(w, h, oldw, oldh)
    }


}