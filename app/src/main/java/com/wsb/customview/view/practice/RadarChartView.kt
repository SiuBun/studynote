package com.wsb.customview.view.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class PolygonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var sides: Int = 3
    private var layers: Int = 1
    private var maxValue: Float = 1f
    private var data: Map<String, Float> = emptyMap()
    private val dataPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = resources.displayMetrics.density
        color = 0x550000FF // 半透明蓝色
    }
    private var layerColors: List<Int> = listOf(0xFF000000.toInt()) // 默认黑色
    private val layerPaints = mutableListOf<Paint>()

    fun setPolygonData(map: Map<String, Float>, layers: Int, maxValue: Float) {
        this.data = map
        this.sides = map.size
        this.layers = layers
        this.maxValue = maxValue
        invalidate()
    }

    fun setLayerColors(colors: List<Int>) {
        this.layerColors = colors
        layerPaints.clear()
        colors.forEach { color ->
            layerPaints.add(Paint().apply {
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = 2f
                this.color = color
            })
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sides < 3) return

        val centerX = width / 2f
        val centerY = height / 2f
        val maxRadius = Math.min(centerX, centerY)
        val layerDistance = maxRadius / layers

        // 绘制多边形层
        for (i in layers downTo 1) {
            val path = Path()
            val radius = i * layerDistance
            for (j in 0 until sides) {
                val angle = 2 * Math.PI * j / sides // 计算当前点的角度
                val x = (centerX + radius * Math.cos(angle)).toFloat() // 计算x坐标
                val y = (centerY + radius * Math.sin(angle)).toFloat() // 计算y坐标
                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()
            val paint = layerPaints[(i - 1) % layerPaints.size] // 轮流使用颜色
            canvas.drawPath(path, paint)
        }

        // 绘制雷达图数据
        if (data.isNotEmpty()) {
            val dataPath = Path()
            data.entries.forEachIndexed { index, entry ->
                val value = entry.value
                val angle = 2 * Math.PI * index / sides // 计算当前点的角度
                val radius = (value / maxValue) * maxRadius // 计算当前点的半径
                val x = (centerX + radius * Math.cos(angle)).toFloat() // 计算x坐标
                val y = (centerY + radius * Math.sin(angle)).toFloat() // 计算y坐标
                if (index == 0) {
                    dataPath.moveTo(x, y)
                } else {
                    dataPath.lineTo(x, y)
                }
            }
            dataPath.close()
            canvas.drawPath(dataPath, dataPaint)
        }
    }
}




























