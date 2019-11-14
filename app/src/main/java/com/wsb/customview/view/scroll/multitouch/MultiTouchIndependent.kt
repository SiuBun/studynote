package com.wsb.customview.view.scroll.multitouch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.wsb.customview.utils.DrawUtils

class MultiTouchIndependent @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val pathArray: SparseArray<Path> = SparseArray()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = DrawUtils.dp2px(4F)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

//    private var path: Path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            for (index in 0 until pathArray.size()) {
                drawPath(pathArray.valueAt(index), paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            when (actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val path = Path().also {
                        it.moveTo(getX(actionIndex), getY(actionIndex))
                    }
                    pathArray.append(getPointerId(actionIndex), path)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    for (index in 0 until pointerCount) {
                        pathArray.get(getPointerId(index)).lineTo(getX(index), getY(index))
                    }
                    invalidate()
                }
                MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                    pathArray[getPointerId(actionIndex)].reset()
                    pathArray.remove(getPointerId(actionIndex))
                    invalidate()
                }
                else -> {
                }
            }
        }
        return true
    }
}