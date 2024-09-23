package com.wsb.customview.view.scroll.scalable

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import com.wsb.customview.utils.DrawUtils
import com.wsb.customview.view.scroll.scalable.state.ScaleStateManager
import com.wsb.customview.view.scroll.scalable.state.ScrollCallback
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

/**
 * 支持放大缩小的图片控件
 *
 * @author wsb
 * */
class MultiTouchScaleImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScrollCallback {

    /**
     * 惯性滑动时候通知变化
     * */
    private val flingRunnable: Runnable = Runnable { scrollRefresh() }

    /**
     * 准备加载的图片
     * */
    private var bitmap: Bitmap = DrawUtils.getBitmap(resources, DrawUtils.dp2px(150F))

    /**
     * 画笔对象
     * */
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 图片居中所需要的X轴偏移量
     * */
    private var canvasOffsetX: Float = 0F

    /**
     * 图片居中所需要的Y轴偏移量
     * */
    private var canvasOffsetY: Float = 0F

    /**
     * 图片放大至边距触屏所应该放大的倍数
     * */
    private var scaleAmountMax: Float = 0F


    /**
     * 图片缩小回原型所应该缩小的倍数
     * */
    private var scaleAmountMin: Float = 0F

    /**
     * 缩放进度
     * */
    private var curScaleFactor: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 手势处理对象
     * */
    private val gestureDetectorCompat: GestureDetectorCompat = GestureDetectorCompat(context, this)

    private var scroll: OverScroller = OverScroller(context)

    private var initialCurrentScale: Float = 0F
    private var onScaleGestureDetectorListener: ScaleGestureDetector.OnScaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            initialCurrentScale = curScaleFactor
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {

        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            获取当前倍数
            detector?.run {
                curScaleFactor = scaleFactor * initialCurrentScale
//            倍数应用到实际参数中
                invalidate()
            }
            return false
        }

    }

    private var scaleDetector: ScaleGestureDetector = ScaleGestureDetector(context, onScaleGestureDetectorListener)

    /**
     * 放大动画
     * */
    private var scaleAnimator: ObjectAnimator = ObjectAnimator.ofFloat(this, "curScaleFactor", 0F).apply {
        duration = TimeUnit.MILLISECONDS.toMillis(200)
    }
        get() {
            field.setFloatValues(scaleAmountMin, scaleAmountMax)
            return field
        }


    /**
     * 滑动处理对象
     * */
    private val scaleStateManager: ScaleStateManager = ScaleStateManager(this)


    /**
     * 当前X轴滑动距离
     * */
    private var scrollOffsetX: Float = 0F

    /**
     * 当前Y轴滑动距离
     * */
    private var scrollOffsetY: Float = 0F


    /**
     * 放大后X轴滑动距离
     * */
    private var maxScrollOffsetX: Float = 0F

    /**
     * 放大后Y轴滑动距离
     * */
    private var maxScrollOffsetY: Float = 0F

    companion object {
        const val OVER_SCALE_FACTOR = 1.5f


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (scaleAnimator.isRunning) {
            scaleAnimator.cancel()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        canvasOffsetX = (width - bitmap.width).toFloat() / 2
        canvasOffsetY = (height - bitmap.height).toFloat() / 2

//        当图片为窄图的时候,放大倍数应该是以屏幕宽度和图片宽度的比例,反之扁图放大倍数应该是屏幕高度和图片高度的比例,可以乘以放大系数增强放大效果
        scaleAmountMax = (if (DrawUtils.narrowBitmap(bitmap)) width / bitmap.width else height / bitmap.height) * OVER_SCALE_FACTOR

//        当图片为窄图时候,缩小的倍数应该是屏幕高度除以图片高度,扁图应该是屏幕宽度除以图片宽度
        scaleAmountMin = (if (DrawUtils.narrowBitmap(bitmap)) height / bitmap.height else width / bitmap.width).toFloat()

        maxScrollOffsetX = (bitmap.width * scaleAmountMax - width) / 2
        maxScrollOffsetY = (bitmap.height * scaleAmountMax - height) / 2

        curScaleFactor = scaleAmountMin
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.run {
            save()
            val scaleFraction = (curScaleFactor - scaleAmountMin) / (scaleAmountMax - scaleAmountMin)
            translate(scrollOffsetX * scaleFraction, scrollOffsetY * scaleFraction)
//            val scale = (scaleAmountMin + (scaleAmountMax - scaleAmountMin) * scaleFraction)
//                    .also { LogUtils.d("放大倍数为$it") }
            scale(curScaleFactor, curScaleFactor, (width / 2F), (height / 2F))
            drawBitmap(bitmap, canvasOffsetX, canvasOffsetY, paint)
            restore()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var result = scaleDetector.onTouchEvent(event)
        if (!scaleDetector.isInProgress) {
            result = gestureDetectorCompat.onTouchEvent(event)
        }
        return result
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        scaleStateManager.onViewFling(velocityX, velocityY)
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        scaleStateManager.onScroll(distanceX, distanceY)
        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        e?.run {
            scaleStateManager.onViewDoubleTap(scaleAnimator, this)
            scaleStateManager.changeState()
        }
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }

    override fun scrollImage(offsetX: Float, offsetY: Float) {
        this.scrollOffsetX -= offsetX
        this.scrollOffsetY -= offsetY
        fixOffset()
        invalidate()
    }

    private fun fixOffset() {
        scrollOffsetX = max(min(scrollOffsetX, maxScrollOffsetX), -maxScrollOffsetX)
        scrollOffsetY = max(min(scrollOffsetY, maxScrollOffsetY), -maxScrollOffsetY)
    }

    override fun onViewFling(velocityX: Float, velocityY: Float) {
        scroll.fling(scrollOffsetX.toInt(), scrollOffsetY.toInt(), velocityX.toInt(), velocityY.toInt()
                , -maxScrollOffsetX.toInt(), maxScrollOffsetX.toInt(), -maxScrollOffsetY.toInt(), maxScrollOffsetY.toInt()
                , DrawUtils.dp2px(40F).toInt(), DrawUtils.dp2px(40F).toInt()
        )

        ViewCompat.postOnAnimation(this, flingRunnable)
    }

    private fun scrollRefresh() {
        val computeScrollOffset = scroll.computeScrollOffset()
        if (computeScrollOffset) {
            scrollOffsetX = scroll.currX.toFloat()
            scrollOffsetY = scroll.currY.toFloat()
            invalidate()
            postOnAnimation(flingRunnable)
        }
    }

    override fun onViewDoubleTapFromSmall(motionEvent: MotionEvent) {
        scrollOffsetX = (motionEvent.x - (width / 2F)) * (1 - scaleAmountMax / scaleAmountMin)
        scrollOffsetY = (motionEvent.y - (height / 2F)) * (1 - scaleAmountMax / scaleAmountMin)
        fixOffset()
    }


}
