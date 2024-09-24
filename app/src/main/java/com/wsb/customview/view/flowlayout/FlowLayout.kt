package com.wsb.customview.view.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

/**
 * FlowLayout是一个支持流式布局的ViewGroup子类
 * 允许在xml内部使用,把相关控件声明在其内部后,能根据内部控件的宽度决定是否换行展示
 * 同时支持外部addView的方式加入新子view进行ui更新
 */
class FlowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    // 当前行的高度
    private var lineHeight = 0
    // 当前行的宽度
    private var lineWidth = 0
    // 存储所有行的子视图
    private val lines = mutableListOf<MutableList<View>>()
    // 存储每行的宽度
    private val lineWidths = mutableListOf<Int>()

    /**
     * 测量子View的大小并确定FlowLayout的宽高
     *
     * 1. 根据自身测量规格,获取宽度和高度的测量模式和大小.MeasureSpec.getMode, MeasureSpec.getSize
     * 2. 根据自身测量模式和最大宽度，计算子View的测量规格.MeasureSpec.makeMeasureSpec
     * 3. 基于子View测量规格,遍历所有子View进行测量.measureChild.同时记录宽高便于换行
     * 4. 根据子View的测量结果，计算FlowLayout的宽高
     * 5. 根据自身测量模式,得到最终测量结果(EXACTLY则用自己的,ALMOST则用记录结果和自己的取小)
     * 6. 测量结果设置测量的尺寸.setMeasuredDimension
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 根据测量规格,获取宽度和高度的测量模式和大小
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // 遍历子视图时，存储当前行的最大宽度,
        var width = 0
        var height = 0

        // 考虑 FlowLayout 的 padding
        val horizontalPadding = paddingLeft + paddingRight
        val verticalPadding = paddingTop + paddingBottom

        // 重置测量变量
        lineWidth = paddingLeft
        lineHeight = 0
        lines.clear()
        lineWidths.clear()

        var currentLine = mutableListOf<View>()
        lines.add(currentLine)

        // 创建子视图的测量规格，考虑 FlowLayout 的 padding.
        // 应根据父视图的测量模式来决定子视图的测量模式。
        // 具体来说，如果父视图的测量模式是 EXACTLY，那么子视图也应该使用 EXACTLY 模式进行测量；如果父视图的测量模式是 AT_MOST 或 UNSPECIFIED，则子视图使用 AT_MOST 模式进行测量
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            widthSize - horizontalPadding,
            if (widthMode == MeasureSpec.EXACTLY) MeasureSpec.EXACTLY else MeasureSpec.AT_MOST
        )
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            heightSize - verticalPadding,
            if (heightMode == MeasureSpec.EXACTLY) MeasureSpec.EXACTLY else MeasureSpec.AT_MOST
        )

        // 遍历所有子视图进行测量
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                // 测量子视图
                measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec)
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight

                // 考虑子视图的 margin
                val lp = child.layoutParams as MarginLayoutParams
                val childWidthWithMargin = childWidth + lp.leftMargin + lp.rightMargin
                val childHeightWithMargin = childHeight + lp.topMargin + lp.bottomMargin

                // 检查是否需要换行: 如果当前行的宽度加上当前子视图的宽度（加上 margin）大于 FlowLayout 的能提供的最大宽度，那么需要换行
                if (lineWidth + childWidthWithMargin > widthSize - paddingRight && currentLine.isNotEmpty()) {
                    width = max(width, lineWidth)
                    lineWidths.add(lineWidth)
                    lineWidth = paddingLeft + childWidthWithMargin
                    height += lineHeight
                    lineHeight = childHeightWithMargin
                    currentLine = mutableListOf()
                    lines.add(currentLine)
                } else {
                    lineWidth += childWidthWithMargin
                    lineHeight = max(lineHeight, childHeightWithMargin)
                }
                currentLine.add(child)
            }
        }

        // 处理最后一行,width将存储所有行中最宽的一行的宽度
        width = max(width, lineWidth)
        lineWidths.add(lineWidth)
        height += lineHeight

        // 添加 FlowLayout 的 padding 到总宽度和高度
        width += paddingRight
        height += verticalPadding

        // 根据测量模式调整最终的宽度和高度
        width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(width, widthSize)
            else -> width
        }

        height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(height, heightSize)
            else -> height
        }

        // 设置测量的尺寸
        setMeasuredDimension(width, height)
    }

    /**
     * 布局子View的位置
     *
     * 1. 遍历每一行,child.layout,传入其左上右下的位置(考虑margin),更新下一个子View的左边界
     * 2. 遍历每一行,更新下一行的顶部位置,考虑当前行中最高的子View(包括margin)
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = paddingTop
        var left: Int

        // 遍历每一行进行布局
        for ((lineIndex, line) in lines.withIndex()) {
            left = paddingLeft
            val lineWidth = lineWidths[lineIndex]
            for (child in line) {
                if (child.visibility != View.GONE) {
                    // 获取子视图的 LayoutParams，并转换为 MarginLayoutParams
                    val lp = child.layoutParams as MarginLayoutParams
                    // 计算子视图的位置，考虑 margin
                    val childLeft = left + lp.leftMargin
                    val childTop = top + lp.topMargin
                    val childRight = childLeft + child.measuredWidth
                    val childBottom = childTop + child.measuredHeight
                    // 布局子视图
                    child.layout(childLeft, childTop, childRight, childBottom)
                    // 更新下一个子视图的左边界
                    left += child.measuredWidth + lp.leftMargin + lp.rightMargin
                }
            }
            // 更新下一行的顶部位置，考虑当前行中最高的子视图（包括其 margin）
            top += line.maxOf { (it.layoutParams as MarginLayoutParams).topMargin + it.measuredHeight + (it.layoutParams as MarginLayoutParams).bottomMargin }
        }
    }

    /**
     * 生成带有 margin 的 LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 生成默认的 LayoutParams
     */
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    /**
     * 确保所有子视图都使用 MarginLayoutParams
     */
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    /**
     * 检查 LayoutParams 是否为 MarginLayoutParams 类型
     */
    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }
}