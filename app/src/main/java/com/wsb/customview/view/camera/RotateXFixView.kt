package com.wsb.customview.view.camera

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.wsb.customview.MainActivity
import com.wsb.customview.R
import kotlin.math.sin

class RotateXFixView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    var degree: Float = 0F
    set(value) {
        field = value
        invalidate()
    }

    private var animator = ObjectAnimator.ofFloat(this, "degree", 0F, 360F)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.end()
    }

    init {
        animator.duration = 5000
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)

        var point1 = Point(100, 100)
        var point2 = Point(300, 200)

        var camera = Camera()
        var matrix = Matrix()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val center1X = point1.x + bitmapWidth / 2
        val center1Y = point1.y + bitmapHeight / 2
        val center2X = point2.x + bitmapWidth / 2
        val center2Y = point2.y + bitmapHeight / 2

        camera.save()
        matrix.reset()
        camera.setLocation(0F,0F,-16F)
        camera.rotateX(degree)
        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate((-center1X).toFloat(), (-center1Y).toFloat())
        matrix.postTranslate(center1X.toFloat(), center1Y.toFloat())

        canvas?.run {
            save()
            concat(matrix)
            drawBitmap(bitmap, point1.x.toFloat(), point1.y.toFloat(), paint)
            restore()
        }

        camera.save()
        matrix.reset()
        camera.rotateY(30f)
        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate((-center2X).toFloat(), (-center2Y).toFloat())
        matrix.postTranslate(center2X.toFloat(), center2Y.toFloat())

        canvas?.run {
            save()
            concat(matrix)
            drawBitmap(bitmap, point2.x.toFloat(), point2.y.toFloat(), paint)
            restore()
        }
    }
}