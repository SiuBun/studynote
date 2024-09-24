package com.wsb.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.sin

/**
 * WaveView是一个自定义View类，用于绘制波浪动画效果。
 * 该类允许外部指定宽高，并提供开始和结束方法来控制动画。
 * 支持外部传参更新波浪的高度和频率。
 */

class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 波浪的高度
    private var waveHeight: Float = 100f
    // 波浪的频率
    private var waveFrequency: Float = 1f
    // 波浪的相位
    private var wavePhase: Float = 0f
    // 第二条波浪的相位
    private var secondWavePhase: Float = 0f
    // 动画是否正在进行
    private var isAnimating: Boolean = false
    // 动画速度
    private var waveSpeed: Float = 0.1f
    // 第二条波浪的相位偏移
    private var secondWavePhaseOffset: Float = 45f

    // 用于绘制波浪的画笔
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

    // 预计算的常量
    private val DEG_TO_RAD = Math.PI / 180

    init {
        // 初始化代码
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
     * 绘制波浪的方法
     * @param canvas 画布
     * @param path 波浪路径
     * @param paint 波浪画笔
     * @param phase 波浪相位
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
}

// 在Activity中使用WaveView的示例代码
/*
class MainActivity : AppCompatActivity() {

    private lateinit var waveView: WaveView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化WaveView
        waveView = findViewById(R.id.waveView)

        // 开始波浪动画
        waveView.startWaveAnimation()

        // 设置波浪高度
        waveView.setWaveHeight(150f)

        // 设置波浪频率
        waveView.setWaveFrequency(2f)

        // 设置波浪相位
        waveView.setWavePhase(0.5f)

        // 设置动画速度
        waveView.setWaveSpeed(0.2f)

        // 设置第二条波浪的相位偏移
        waveView.setSecondWavePhaseOffset(45f)

        // 判断当前是否在进行波浪动画
        val isAnimating = waveView.isWaveAnimating()
        println("当前是否在进行波浪动画: $isAnimating")

        // 停止波浪动画
        // waveView.stopWaveAnimation()
    }
}
*/

