package com.wsb.customview.view.camera

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.wsb.customview.R

class FlipboardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bitmap:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.maps)
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var degree = 30F
    set(value) {
        field = value
        invalidate()
    }

    private var animator = ObjectAnimator.ofFloat(this, "degree", 0F, 180F).apply {
        duration = 5000
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
    }

    private var camera:Camera = Camera()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.end()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.run {

            var matrix = Matrix()
//            控件中心
            val centerX:Float = (width / 2).toFloat()
            val centerY:Float = (height / 2).toFloat()

//            图片最左边的left值和top值
            val bitmapStartX:Float = (centerX - bitmap.width / 2)
            val bitmapStartY:Float = (centerY - bitmap.height / 2)

            save()
//            绘制上半部分本质是切割留下上一半
            clipRect(bitmapStartX,bitmapStartY,width.toFloat(), centerY)
            drawBitmap(bitmap,bitmapStartX,bitmapStartY,paint)
            restore()



            camera.save()
            camera.rotateX(degree)
            camera.setLocation(0F,0F,-16F)
            camera.getMatrix(matrix)
            camera.restore()

            matrix.preTranslate(-centerX,-centerY)
            matrix.postTranslate(centerX,centerY)

            save()
            concat(matrix)
            clipRect(bitmapStartX,centerY,width.toFloat(),height.toFloat())
            drawBitmap(bitmap,bitmapStartX,bitmapStartY,paint)
            restore()
        }
    }
}