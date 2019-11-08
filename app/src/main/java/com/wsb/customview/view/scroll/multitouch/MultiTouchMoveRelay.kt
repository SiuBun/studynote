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

class MultiTouchMoveRelay @JvmOverloads constructor(
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
            when (actionMasked) {
                MotionEvent.ACTION_DOWN -> {
//                    指定由第0个针点为处理对象
                    onPointerIdChange(0)
                }

                MotionEvent.ACTION_MOVE -> {
//                    接收被指定处理的针点id的坐标变化
                    val pointerIndex = findPointerIndex(executePointerId)
                    imageOffsetX = getX(pointerIndex) - downX + initOffsetX
                    imageOffsetY = getY(pointerIndex) - downY + initOffsetY

                    fixOffset()
                    invalidate()
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    onPointerIdChange(
                            actionIndex.also {LogUtils.d("当前接收到的针点下标为$it")}
                    )
                }

                MotionEvent.ACTION_POINTER_UP -> {
//                    抬起针点可能是双指中的一指,也可能是三指中随便一指.0,1,2
                    if (actionIndex == findPointerIndex(executePointerId)) {
                        LogUtils.d("抬起针点为现处理移动的针点")
                        val newIndex: Int = if (actionIndex == pointerCount - 1) {
//                            如果抬起针点为双指的其中一个,此时count数仍包括该针点,那么要取-2位的针点下标来处理后续事情
                            pointerCount - 2
                        } else {
//                            如果抬起针点为列表其中一个,那么要取其前一位的针点
                            pointerCount - 1
                        }
                        onPointerIdChange(newIndex)
                    }

                    LogUtils.d("现指针列表个数为$pointerCount")
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

    private fun fixOffset() {
        imageOffsetX = min(width.toFloat() - bitmap.width, max(0F, imageOffsetX))
        imageOffsetY = min(height.toFloat() - bitmap.height, max(0F, imageOffsetY))
    }
}