package com.wsb.customview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.wsb.customview.MainActivity
import com.wsb.customview.R
import kotlin.math.sin

class RotateXFixView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)

        var point1 = Point(200, 200)
        var point2 = Point(600, 200)

        var camera = Camera()
        var matrix = Matrix()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val center1X = point1.x + bitmapWidth / 2
        val center1Y = point1.y + bitmapHeight / 2
        val center2X = point2.x + bitmapWidth / 2
        val center2Y = point2.y + bitmapHeight / 2

        val angle = (Math.PI * 2 / 5).toDouble()
        Log.d(MainActivity.TAG,"得到角度为$angle , 正弦值为${sin(angle*2)}")

        camera.save()
        matrix.reset()
        camera.rotateX(30f)
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