package com.wsb.customview.view.scroll.multitouch

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wsb.customview.R
import com.wsb.customview.utils.LogUtils
import kotlin.math.max
import kotlin.math.min

class MultiTouchMovecooperation @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var bitmap = BitmapFactory.decodeResource(resources, R.drawable.what_the_fuck)

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var imageOffsetX = 0F
    private var imageOffsetY = 0F

    private var downX = 0F
    private var downY = 0F

    private var initOffsetX = 0F
    private var initOffsetY = 0F

    private var executePointerId: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawBitmap(bitmap, imageOffsetX, imageOffsetY, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        event?.run {
            var sumX = 0f
            var sumY = 0f

            var pointerCount = pointerCount
            val pointerUp = MotionEvent.ACTION_POINTER_UP == actionMasked

            for (index in 0 until pointerCount) {
//                抬起时候的手指不计入本次计算的内容
                if (!pointerUp || index != actionIndex) {
                    sumX += getX(index)
                    sumY += getY(index)
                }
            }

//            某个手指抬起的时候应该把计算剩下的手指,而不是把当前总手指数算入
            if (pointerUp) {
                pointerCount--
            }

            val cooperatePointX = sumX / pointerCount
            val cooperatePointY = sumY / pointerCount
            LogUtils.d("协调点为$cooperatePointX 和$cooperatePointY")

            when (actionMasked) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN,
                MotionEvent.ACTION_POINTER_UP -> {
                    LogUtils.d("当前接收到的针点下标为$actionIndex")
//                    指定由第0个针点为处理对象
                    onPointerIdChange(cooperatePointX, cooperatePointY)
                }

                MotionEvent.ACTION_MOVE -> {
//                    接收被指定处理的针点id的坐标变化
                    imageOffsetX = cooperatePointX - downX + initOffsetX
                    imageOffsetY = cooperatePointY - downY + initOffsetY

//                    fixOffset()
                    invalidate()
                }

                else -> {
                }
            }
        }
        return true
    }

    private fun MotionEvent.onPointerIdChange(pointerIndex: Int) {
        executePointerId = getPointerId(pointerIndex)
        // 记录用于在图片做偏移时候考虑偏移的量
        downX = getX(pointerIndex)
        downY = getY(pointerIndex)

        // 赋值用于第二次点击时候,位移在之前已经偏移的量基础上位移
        initOffsetX = imageOffsetX
        initOffsetY = imageOffsetY
    }

    private fun onPointerIdChange(cooperatePointX: Float, cooperatePointY: Float) {
        // 记录用于在图片做偏移时候考虑偏移的量
        downX = cooperatePointX
        downY = cooperatePointY

        // 赋值用于第二次点击时候,位移在之前已经偏移的量基础上位移
        initOffsetX = imageOffsetX
        initOffsetY = imageOffsetY
    }

    private fun fixOffset() {
        imageOffsetX = min(width.toFloat() - bitmap.width, max(0F, imageOffsetX))
        imageOffsetY = min(height.toFloat() - bitmap.height, max(0F, imageOffsetY))
    }
}