package com.wsb.customview.view.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

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

    /**
     * 测量子View的大小并确定FlowLayout的宽高
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = 0
        var height = 0
        var rowWidth = 0
        var rowHeight = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // 考虑FlowLayout的padding
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        val maxWidth = widthSize - paddingLeft - paddingRight

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val lp = child.layoutParams as MarginLayoutParams

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            // 如果当前行的宽度加上子View的宽度超过了父View的宽度，则换行
            if (rowWidth + childWidth > maxWidth) {
                // 更新FlowLayout的宽度为当前行的最大宽度
                width = maxOf(width, rowWidth)
                // 累加FlowLayout的高度
                height += rowHeight
                // 重置当前行的宽度和高度
                rowWidth = childWidth
                rowHeight = childHeight
            } else {
                // 累加当前行的宽度
                rowWidth += childWidth
                // 更新当前行的最大高度
                rowHeight = maxOf(rowHeight, childHeight)
            }
        }

        // 更新FlowLayout的宽度为所有行中最大宽度
        width = maxOf(width, rowWidth) + paddingLeft + paddingRight
        // 累加最后一行的高度
        height += rowHeight + paddingTop + paddingBottom

        // 设置FlowLayout的宽高
        setMeasuredDimension(
            if (widthMode == MeasureSpec.EXACTLY) widthSize else width,
            if (heightMode == MeasureSpec.EXACTLY) heightSize else height
        )
    }

    /**
     * 布局子View的位置
     */
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var x = paddingLeft
        var y = paddingTop
        var rowHeight = 0
        val maxWidth = width - paddingRight

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            // 如果当前行的宽度加上子View的宽度超过了父View的宽度，则换行
            if (x + childWidth > maxWidth) {
                // 重置x坐标
                x = paddingLeft
                // 累加y坐标
                y += rowHeight
                // 更新当前行的最大高度
                rowHeight = childHeight
            } else {
                // 更新当前行的最大高度
                rowHeight = maxOf(rowHeight, childHeight)
            }

            // 布局子View的位置
            child.layout(
                x + lp.leftMargin,
                y + lp.topMargin,
                x + lp.leftMargin + child.measuredWidth,
                y + lp.topMargin + child.measuredHeight
            )

            // 累加x坐标
            x += childWidth
        }
    }

    /**
     * 生成默认的布局参数
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 生成布局参数
     */
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    /**
     * 生成默认的布局参数
     */
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    /**
     * 检查布局参数是否有效
     */
    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }
}