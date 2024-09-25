package com.wsb.customview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.sin

/**
 * WaveView 是一个自定义 View，用于绘制可交互的波浪动画效果。
 * 支持点击切换动画状态、拖拽调整波浪位置，以及惯性滑动效果。
 */
class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 波浪参数
    private var waveHeight: Float = 100f
    // 波浪的频率
    private var waveFrequency: Float = 1f
    // 波浪的相位
    private var wavePhase: Float = 0f
    // 第二条波浪的相位
    private var secondWavePhase: Float = 0f
    private var waveSpeed: Float = 0.1f
    // 第二条波浪的相位偏移
    private var secondWavePhaseOffset: Float = 45f

    // 动画状态
    private var isAnimating: Boolean = false

    // 画笔和路径
    private val wavePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
        style = Paint.Style.FILL
    }
    // 用于绘制第二条波浪的画笔
    private val secondWavePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
        style = Paint.Style.FILL
    }

    // 用于绘制波浪的路径
    private val wavePath: Path = Path()
    // 用于绘制第二条波浪的路径
    private val secondWavePath: Path = Path()
    // 用于裁剪画布的圆形路径
    private val circlePath: Path = Path()

    // 触摸事件相关参数
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    private var lastTouchTime: Long = 0
    private var isDragging: Boolean = false
    private val CLICK_THRESHOLD = 10 // 点击判定阈值，单位为像素

    // 惯性动画相关参数
    private var velocityX: Float = 0f
    private var inertiaAnimator: ValueAnimator? = null

    // 常量
    private val DEG_TO_RAD = Math.PI / 180

    init {
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val width = w.toFloat()
        val height = h.toFloat()
        val radius = Math.min(width, height) / 2

        // 创建一个圆形路径用于裁剪画布
        circlePath.reset()
        circlePath.addCircle(width / 2, height / 2, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 裁剪画布
        canvas.clipPath(circlePath)

        drawWave(canvas, wavePath, wavePaint, wavePhase)
        drawWave(canvas, secondWavePath, secondWavePaint, secondWavePhase + secondWavePhaseOffset)

        if (isAnimating) {
            wavePhase += waveSpeed
            secondWavePhase += waveSpeed * 1.5f // 使第二条波浪的速度稍微不同
            invalidate()
        }
    }

    /**
     * 处理触摸事件，实现点击切换动画状态、拖拽调整波浪位置和惯性滑动效果
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 停止正在进行的惯性动画
                inertiaAnimator?.cancel()

                lastTouchX = event.x
                lastTouchY = event.y
                lastTouchTime = System.currentTimeMillis()
                isDragging = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - lastTouchX
                val deltaY = event.y - lastTouchY
                val currentTime = System.currentTimeMillis()
                val deltaTime = currentTime - lastTouchTime

                if (!isDragging && (abs(deltaX) > CLICK_THRESHOLD || abs(deltaY) > CLICK_THRESHOLD)) {
                    isDragging = true
                }

                if (isDragging) {
                    updateWavePhase(deltaX)
                    // 计算速度
                    if (deltaTime > 0) {
                        velocityX = deltaX / deltaTime * 1000 // 转换为像素/秒
                    }
                    lastTouchX = event.x
                    lastTouchY = event.y
                    lastTouchTime = currentTime
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (!isDragging) {
                    toggleAnimation()
                } else {
                    // 开始惯性动画
                    startInertiaAnimation()
                }
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 开始惯性动画
     */
    private fun startInertiaAnimation() {
        inertiaAnimator?.cancel()

        inertiaAnimator = ValueAnimator.ofFloat(velocityX, 0f).apply {
            duration = 1500 // 动画持续时间，可以根据需要调整
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                val velocity = animation.animatedValue as Float
                val deltaX = velocity * 16 / 1000 // 假设 16ms 每帧，将速度转换为位移
                updateWavePhase(deltaX)
            }
            start()
        }
    }

    /**
     * 更新波浪相位
     */
    private fun updateWavePhase(deltaX: Float) {
        val phaseShift = deltaX * 0.1f
        wavePhase -= phaseShift
        secondWavePhase -= phaseShift
        invalidate()
    }

    /**
     * 绘制单个波浪
     */
    private fun drawWave(canvas: Canvas, path: Path, paint: Paint, phase: Float) {
        path.reset()
        val width = width.toFloat()
        val height = height.toFloat()
        path.moveTo(0f, height / 2)

        var previousX = 0f
        var previousY = height / 2

        for (x in 0..width.toInt()) {
            val y = (waveHeight * sin((x + phase) * waveFrequency * DEG_TO_RAD)).toFloat() + height / 2
            path.quadTo(previousX, previousY, x.toFloat(), y)
            previousX = x.toFloat()
            previousY = y
        }

        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()

        canvas.drawPath(path, paint)
    }

    /**
     * 切换动画状态
     */
    private fun toggleAnimation() {
        isAnimating = !isAnimating
        invalidate()
    }

    /**
     * 开始波浪动画
     */
    fun startWaveAnimation() {
        isAnimating = true
        invalidate()
    }

    /**
     * 停止波浪动画
     */
    fun stopWaveAnimation() {
        isAnimating = false
    }

    /**
     * 设置波浪高度
     * @param height 波浪高度
     */
    fun setWaveHeight(height: Float) {
        waveHeight = height
        invalidate()
    }

    /**
     * 设置波浪频率
     * @param frequency 波浪频率
     */
    fun setWaveFrequency(frequency: Float) {
        waveFrequency = frequency
        invalidate()
    }

    /**
     * 设置波浪相位
     * @param phase 波浪相位
     */
    fun setWavePhase(phase: Float) {
        wavePhase = phase
        secondWavePhase = phase
        invalidate()
    }

    /**
     * 设置动画速度
     * @param speed 动画速度
     */
    fun setWaveSpeed(speed: Float) {
        waveSpeed = speed
        invalidate()
    }

    /**
     * 设置第二条波浪的相位偏移
     * @param offset 相位偏移
     */
    fun setSecondWavePhaseOffset(offset: Float) {
        secondWavePhaseOffset = offset
        invalidate()
    }

    /**
     * 判断当前是否在进行波浪动画
     * @return 是否在进行动画
     */
    fun isWaveAnimating(): Boolean {
        return isAnimating
    }

    /**
     * 获取当前波浪相位
     */
    fun getWavePhase(): Float {
        return wavePhase
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        inertiaAnimator?.cancel()
    }
}



